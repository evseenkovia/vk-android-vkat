// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("vkid.manifest.placeholders") version "1.1.0" apply true
}

buildscript {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.google.com/")
        }
    }
    dependencies {
        // Для плагинов Gradle
        classpath(libs.gradle)
        classpath(libs.com.google.devtools.ksp.gradle.plugin)
    }
}

// Добавление значений в Manifest Placeholders.
vkidManifestPlaceholders {
    // Или укажите значения явно через properties, если не хотите использовать плейсхолдеры.
    vkidRedirectHost = "vk.ru" // Обычно vk.ru.
    vkidRedirectScheme = "vk1233445" // Строго в формате vk{ID приложения}.
    vkidClientId = "1234567"
    vkidClientSecret = "ваш_защищенный_ключ_из_панели_vk"
}