package com.example.vk_android_vkat.config

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.vk.id.AccessToken
import com.vk.id.VKIDAuthFail

import com.vk.id.onetap.common.OneTapOAuth // Импортируйте правильный класс OAuth

fun getOneTapSuccessCallback(
    context: Context,
    onTokenReceived: (AccessToken) -> Unit
): (OneTapOAuth?, AccessToken) -> Unit { // Теперь принимает 2 параметра
    return { oAuth, accessToken ->
        val providerName = oAuth?.name ?: "VK"
        Toast.makeText(context, "Вход через $providerName успешен!", Toast.LENGTH_SHORT).show()

        // Передаем токен дальше в состояние экрана
        onTokenReceived(accessToken)
    }
}

fun getOneTapFailCallback(context: Context): (OneTapOAuth?, VKIDAuthFail) -> Unit { // Теперь принимает 2 параметра
    return { oAuth, fail ->
        val providerName = oAuth?.name ?: "VK"
        val errorMessage = when (fail) {
            is VKIDAuthFail.Canceled -> "Вход через $providerName отменен"
            is VKIDAuthFail.NoBrowserAvailable -> "Нет интернета для подключения к $providerName"
            else -> "Ошибка ($providerName): ${fail.description}"
        }
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }
}

@Composable
fun UseToken(accessToken: AccessToken) {
    Column {
        Text(text = "Вы успешно вошли!")
        Text(text = "Ваш токен: ${accessToken.token.take(15)}...")
        Text(text = "ID пользователя: ${accessToken.userID}")
    }
}


