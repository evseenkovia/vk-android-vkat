package com.example.vk_android_vkat.common.di

import androidx.room.Room
import com.example.vk_android_vkat.features.explore.data.local.AppDatabase
import com.example.vk_android_vkat.features.explore.data.remote.RouteRepositoryMock
import com.example.vk_android_vkat.features.explore.domain.RouteRepository
import com.example.vk_android_vkat.features.explore.routeinfo.ui.RouteInfoViewModel
import com.example.vk_android_vkat.features.explore.ui.ExploreViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

// AppModule.kt
val appModule = module {
    // Database - single (один экземпляр на всё приложение)
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "mymaps-db"
        ).build()
    }

    // DAO - factory (новый экземпляр при каждом запросе, легковесный)
    factory { get<AppDatabase>().routeDao() }

    // Repository - single или factory в зависимости от потребностей
    single<RouteRepository> { RouteRepositoryMock(get()) }

    // ViewModel - специальный тип для ViewModel
    viewModel<ExploreViewModel> { ExploreViewModel(get()) }
    viewModel<RouteInfoViewModel> { (routeId: Int) ->
        RouteInfoViewModel(routeId, get())
    }
}