package com.example.vk_android_vkat

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.vk_android_vkat.common.theme.MyMapsTheme

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