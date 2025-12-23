package com.example.vk_android_vkat

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.vk_android_vkat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val navView: BottomNavigationView = binding.navView

//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_explore, R.id.navigation_favourite,
//                R.id.navigation_add, R.id.navigation_map, R.id.navigation_profile
//            )
//        )
        // Устанавливаем BottomNavigation
        navView.setupWithNavController(navController)

        // Управляем видимостью BottomNavigation
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, -> {
                    navView.visibility = View.GONE
                }
                else -> {
                    navView.visibility = View.VISIBLE
                    navView.setupWithNavController(navController)
                }
            }
        }
    }
}