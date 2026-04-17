package com.example.vk_android_vkat.common.di

import androidx.room.Room
import com.example.vk_android_vkat.features.auth.data.AuthInterceptor
import com.example.vk_android_vkat.features.auth.data.AuthRepository
import com.example.vk_android_vkat.features.auth.data.AuthRepositoryImpl
import com.example.vk_android_vkat.features.auth.data.AuthService
import com.example.vk_android_vkat.features.auth.data.TokenManager
import com.example.vk_android_vkat.features.auth.login.LoginViewModel
import com.example.vk_android_vkat.features.auth.registration.RegistrationViewModel
import com.example.vk_android_vkat.features.explore.data.local.AppDatabase
import com.example.vk_android_vkat.features.explore.data.remote.RouteRepositoryMock
import com.example.vk_android_vkat.features.explore.data.remote.RouteService
import com.example.vk_android_vkat.features.explore.domain.RouteRepository
import com.example.vk_android_vkat.features.explore.routeinfo.ui.RouteInfoViewModel
import com.example.vk_android_vkat.features.explore.ui.ExploreViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {

    //для эмулятора 10.0.2.2, для своего устройства - ip из локальной сети
    val BASE_URL = "http://10.0.2.2:8080/"

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

    single<RouteService> {
        get<Retrofit>().create(RouteService::class.java)
    }

    // Repository - single или factory в зависимости от потребностей
    single<RouteRepository> { RouteRepositoryMock(get(), get()) }

    viewModel<ExploreViewModel> { ExploreViewModel(get()) }
    viewModel<RouteInfoViewModel> { (routeId: Int) ->
        RouteInfoViewModel(routeId, get())
    }

    // TokenManager
    single<TokenManager> {
        TokenManager(androidContext())
    }

    // Retrofit
    single<Retrofit> {

        val tokenManager = get<TokenManager>()

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<AuthService> {
        get<Retrofit>().create(AuthService::class.java)
    }

    // repository
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    // viewModel
    viewModel { LoginViewModel(get()) }
    viewModel { RegistrationViewModel(get()) }
}
