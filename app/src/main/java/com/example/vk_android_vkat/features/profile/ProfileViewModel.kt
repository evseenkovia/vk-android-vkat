package com.example.vk_android_vkat.features.profile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk_android_vkat.common.utils.TokenManager
import com.example.vk_android_vkat.features.profile.data.VKUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val vkUserRepository: VKUserRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _state = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val state = _state.asStateFlow()

    init {
        loadProfile()
    }

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.ItemClicked -> handleNavigation(event.id)
            ProfileUiEvent.Retry -> loadProfile()
            is ProfileUiEvent.SwitchChanged -> handleSwitch(event.id, event.checked)
        }
    }

    private fun loadProfile() {
        val currentState = _state.value
        if (currentState is ProfileUiState.Content) {
            // Данные уже есть, не перезагружаем
            return
        }

        viewModelScope.launch {
            _state.value = ProfileUiState.Loading

            try {
                val vkUser = vkUserRepository.getUserInfo()

                if (vkUser != null) {
                    val profileContent = ProfileContentUi(
                        header = ProfileHeaderUi(
                            userName = "${vkUser.firstName} ${vkUser.lastName}".trim(),
                            email = vkUser.email ?: "Email не указан",
                            avatarUrl = vkUser.avatarUrl
                        ),
                        sections = getProfileSections()
                    )
                    _state.value = ProfileUiState.Content(profileContent)
                } else {
                    _state.value = ProfileUiState.Error("Не удалось загрузить данные пользователя")
                }
            } catch (e: Exception) {
                _state.value = ProfileUiState.Error("Ошибка загрузки: ${e.message}")
            }
        }
    }

    /**
     * Секции для отображения в профиле
     */
    private fun getProfileSections(): List<ProfileItemUi> {
        return listOf(
            ProfileItemUi.Info(
                title = "О приложении",
                subtitle = "Версия 1.0.0"
            ),
            ProfileItemUi.Switch(
                title = "Уведомления",
                checked = true
            ),
            ProfileItemUi.Navigation(
                title = "Конфиденциальность",
                section = ProfileSection.Privacy
            ),
            ProfileItemUi.Navigation(
                title = "Выйти из аккаунта",
                section = ProfileSection.About
            )
        )
    }

    private fun handleNavigation(item: ProfileItemUi) {
        when (item.title) {
            "Выйти из аккаунта" -> logout()
        }
    }

    private fun logout() {
        viewModelScope.launch {
            tokenManager.clearToken()
            // Отправьте эффект для перехода на экран входа
        }
    }

    private fun handleSwitch(item: ProfileItemUi, checked: Boolean) {
        val currentContent = (_state.value as? ProfileUiState.Content)?.data ?: return

        val newSections = currentContent.sections.map { sectionItem ->
            when {
                sectionItem is ProfileItemUi.Switch && sectionItem.title == item.title ->
                    sectionItem.copy(checked = checked)
                else -> sectionItem
            }
        }

        _state.value = ProfileUiState.Content(currentContent.copy(sections = newSections))
    }
}
