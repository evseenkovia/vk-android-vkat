package com.example.vk_android_vkat

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.vk_android_vkat.common.theme.MyMapsTheme
import com.example.vk_android_vkat.features.explore.data.local.AppDatabase
import com.yandex.mapkit.MapKitFactory

class MainActivity : AppCompatActivity() {

    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val PERMISSION_REQUEST_CODE = 100
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
        }
        MapKitFactory.getInstance().onStart()
        supportActionBar?.hide()
        setContent {
            MyMapsTheme{
                MyMapsApp()
            }
        }
    }
}
