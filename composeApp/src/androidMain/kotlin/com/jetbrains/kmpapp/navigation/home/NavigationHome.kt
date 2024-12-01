package com.jetbrains.kmpapp.navigation.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.jetbrains.kmpapp.navigation.AppScreens
import com.jetbrains.kmpapp.screens.addProduct.CameraScreenOptionPhoto
import com.jetbrains.kmpapp.screens.addProduct.OptionTakePhotoScreen
import com.jetbrains.kmpapp.screens.home.HomeScreen
import com.jetbrains.kmpapp.screens.home.HomeViewModel
import com.jetbrains.kmpapp.screens.stock.StockScreen

@Composable
fun NavigationHome(modifier: Modifier,navController: NavHostController,homeViewModel: HomeViewModel, navigate:(List<String>)->Unit, visibilityBar:(Boolean)->Unit){

    NavHost(modifier = modifier, navController = navController, startDestination = "home"){
        navigation(startDestination = AppScreens.Home.route, route = "home") {
            composable(AppScreens.Home.route) {
                HomeScreen(navController = navController, viewModel = homeViewModel)
            }

            composable(AppScreens.OptionTakePhoto.route) {
                OptionTakePhotoScreen(navController = navController,
                    navigate = navigate,
                    homeViewModel = homeViewModel,
                    visibilityBar ={visibilityBar(it)}
                )
            }

            composable(AppScreens.Stock.route) {
                StockScreen()
            }

            composable(
                route = AppScreens.CameraScreen.route,
                arguments = listOf(navArgument("typeCamera"){type = NavType.StringType})
            ) {
                val typeCamera = it.arguments?.getString("typeCamera")
                CameraScreenOptionPhoto( typeCamera = typeCamera!!,
                    navigate ={
                        navController.navigateUp()
                        visibilityBar(true)
                    },
                    viewModel = homeViewModel){
                    visibilityBar(it)
                }
            }
        }

    }
}