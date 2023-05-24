package com.anaandreis.trilhaopenai

sealed class Screen(val route : String) {
    object Home: Screen(route = "home_screen")
    object Maps: Screen(route = "maps_screen")
}
