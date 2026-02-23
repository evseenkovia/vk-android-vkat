package com.example.vk_android_vkat.features.explore.data

import com.example.vk_android_vkat.domain.model.RouteModel
import com.example.vk_android_vkat.features.explore.ExploreViewModel
import com.example.vk_android_vkat.features.explore.routeinfo.domain.RouteRepository
import com.example.vk_android_vkat.features.explore.routeinfo.ui.RouteInfoViewModel

class RouteRepositoryImpl(
    val exploreViewModel: ExploreViewModel,
    val routeInfoViewModel: RouteInfoViewModel
) : RouteRepository {

    override fun getRouteById(id: Long) : RouteModel? {
        return exploreViewModel.getRouteById(id)
    }
}