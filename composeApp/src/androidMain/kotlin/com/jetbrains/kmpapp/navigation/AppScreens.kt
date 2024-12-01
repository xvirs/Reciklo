package com.jetbrains.kmpapp.navigation

sealed class AppScreens(val route: String, val title: String) {
    object Home : AppScreens("home_screen", "Home")
    object Stock : AppScreens("stock_screen", "Stock")
    object CameraScreen : AppScreens("camera_screen/{typeCamera}", "Vista de camara")
    object AddProductData : AppScreens("add_product_data_screen", "Alta de producto")
    object OptionTakePhoto : AppScreens("option_take_photo", "Seleccion de foto")
    object DetailProduct : AppScreens("detail_product", "Detalle de producto")
    object LoginScreen : AppScreens("login_screen", "Login")
}