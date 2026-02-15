package com.example.vk_android_vkat.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk_android_vkat.data.mockProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _state = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    init {
        loadProfile()
    }

    fun onEvent(event: ProfileUiEvent){
        when(event) {
            ProfileUiEvent.Retry -> loadProfile()
            is ProfileUiEvent.ItemClicked -> { handleNavigation(event.id) }
            is ProfileUiEvent.SwitchChanged -> { handleSwitch(event.id, event.checked) }
        }
    }

    private fun loadProfile() {
        // Пока мок-данные
        _state.value = mockProfile
    }

    private fun handleNavigation(item: ProfileItemUi) {
        // Здесь можно отправлять событие навигации через Channel, SharedFlow или NavController
        // Пример: navigateTo(item)
    }

    private fun handleSwitch(item: ProfileItemUi, checked: Boolean) {
        val currentContent = (_state.value as? ProfileUiState.Content)?.data ?: return

        val newSections = currentContent.sections.map { sectionItem ->
            when {
                sectionItem is ProfileItemUi.Switch && sectionItem == item ->
                    sectionItem.copy(checked = checked) // безопасно, copy есть только у Switch
                else -> sectionItem
            }
        }

        _state.value = ProfileUiState.Content(currentContent.copy(sections = newSections))
    }

}