package com.example.vk_android_vkat.ui.explore



import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vk_android_vkat.ui.favourite.FavouriteDataStore


class ExploreViewModelFactory(
    private val favouriteDataStore: FavouriteDataStore
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ExploreViewModel(favouriteDataStore) as T
    }
}
