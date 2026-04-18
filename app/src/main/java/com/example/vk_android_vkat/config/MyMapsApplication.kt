package com.example.vk_android_vkat.config

import android.app.Application
import androidx.room.Room
import com.example.vk_android_vkat.BuildConfig
import com.example.vk_android_vkat.common.di.appModule
import com.example.vk_android_vkat.features.explore.data.local.AppDatabase
import com.example.vk_android_vkat.features.explore.data.RouteRepositoryMock
import com.yandex.mapkit.MapKitFactory
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level


class MyMapsApplication : Application() {

    lateinit var routeRepository: RouteRepositoryMock

    override fun onCreate() {
        super.onCreate()
        // Инициализация MapKit один раз для всего приложения
        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
        MapKitFactory.setLocale("ru_RU") //Для Русского текста в адресах
        MapKitFactory.initialize(this)

        startKoin {
            // Логирование (опционально)
            androidLogger(Level.ERROR)

            // Контекст Android
            androidContext(this@MyMapsApplication)

            // Загружаем модули
            modules(appModule)
        }
    }

}
