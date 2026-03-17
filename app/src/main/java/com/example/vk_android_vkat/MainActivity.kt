package com.example.vk_android_vkat

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.vk_android_vkat.common.theme.MyMapsTheme
import com.example.vk_android_vkat.features.explore.data.local.AppDatabase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContent {
            MyMapsTheme{
                MyMapsApp()
            }
        }
    }
}