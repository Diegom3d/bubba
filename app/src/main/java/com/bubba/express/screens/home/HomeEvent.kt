package com.bubba.express.screens.home

sealed class HomeEvent {
    object LoadProducts : HomeEvent()
    data class OnProductClick(val productId: Int) : HomeEvent()
    object OnCartClick : HomeEvent()
    object OnProfileClick : HomeEvent()
    object OnLogoutClick : HomeEvent()
    object OnHistoryClick : HomeEvent()
}