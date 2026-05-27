import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    kotlin("plugin.serialization") version "2.3.10"
    id("com.google.devtools.ksp")
    id("vkid.manifest.placeholders")
}

val mapkitApiKey: String by lazy {
    val props = Properties()
    val localPropsFile = rootProject.file("local.properties")
    if (localPropsFile.exists()) {
        props.load(localPropsFile.inputStream())
    }
    props.getProperty("MAPKIT_API_KEY", "")
}

kotlin {
    compilerOptions {
        languageVersion = org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_3
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

android {
    namespace = "com.example.vk_android_vkat"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.vk_android_vkat"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "MAPKIT_API_KEY", "\"$mapkitApiKey\"")

        addManifestPlaceholders(
            mapOf(
                "VKIDClientID" to "54595531", // ID вашего приложения (app_id).
                "VKIDClientSecret" to "insert_key", // Ваш защищенный ключ (client_secret).
                "VKIDRedirectHost" to "vk.ru", // Обычно используется vk.ru.
                "VKIDRedirectScheme" to "vk54595531", // Должно быть vk{ID приложения}.
            )
        )
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    compileSdkMinor = 1

    configurations.all {
        // Исключаем старую версию аннотаций IntelliJ
        exclude(group = "com.intellij", module = "annotations")
    }
}
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.animation)
    implementation(libs.foundation)
    implementation(libs.foundation)
    // Compose BOM for all compose libraries
    val composeBom = platform("androidx.compose:compose-bom:2026.03.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(libs.maps.mobile)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.coil.compose)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.room)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    ksp(libs.androidx.room.compiler)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // VK ID
    implementation("com.vk.id:vkid:2.7.0")
    implementation("com.vk.id:onetap-compose:2.7.0")

    implementation("io.ktor:ktor-client-core:3.5.0")
    implementation("io.ktor:ktor-client-android:3.5.0")  // Движок для Android
    implementation("io.ktor:ktor-client-content-negotiation:3.5.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.5.0")


}