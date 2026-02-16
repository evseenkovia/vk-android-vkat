package com.example.vk_android_vkat.ui.auth

import androidx.annotation.StringRes
import com.example.vk_android_vkat.R

sealed class AuthError {

    @get:StringRes
    abstract val resId: Int

    object EmailBlank : AuthError() {
        override val resId: Int = R.string.email_blank_error
    }

    object EmailInvalid : AuthError() {
        override val resId: Int = R.string.email_invalid_error
    }

    object EmailNotFound : AuthError() {
        override val resId: Int = R.string.email_not_found_error
    }

    object PasswordBlank : AuthError() {
        override val resId: Int = R.string.password_blank_error
    }

    object PasswordInvalid : AuthError() {
        override val resId: Int = R.string.password_invalid_error
    }

    object PasswordTooShort : AuthError() {
        override val resId: Int = R.string.password_short_error
    }

    object PasswordsMismatch : AuthError() {
        override val resId: Int = R.string.passwords_mismatch_error
    }

    data class Custom(@StringRes val messageRes: Int) : AuthError() {
        override val resId: Int = messageRes
    }
}
