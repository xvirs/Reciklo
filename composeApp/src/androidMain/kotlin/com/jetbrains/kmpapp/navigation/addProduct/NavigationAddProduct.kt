package com.jetbrains.kmpapp.navigation.addProduct

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.jetbrains.kmpapp.R
import com.jetbrains.kmpapp.component.modal.Modal
import com.jetbrains.kmpapp.navigation.AppScreens
import com.jetbrains.kmpapp.screens.addProduct.AddProductDataScreen
import com.jetbrains.kmpapp.screens.addProduct.AddProductViewModel
import com.jetbrains.kmpapp.screens.addProduct.CameraScreenAddProduct
import com.jetbrains.kmpapp.screens.addProduct.DetailProductScreen
import com.jetbrains.kmpapp.screens.home.findActivity
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationAddProduct(navController: NavHostController, addProductViewModel:AddProductViewModel) {

    val context = LocalContext.current
    val activity = context.findActivity()
    val data = Intent()

    NavHost(startDestination = "add_product", navController = navController){
        navigation(startDestination = AppScreens.AddProductData.route, route = "add_product") {
            composable(
                route = AppScreens.CameraScreen.route,
                arguments = listOf(navArgument("typeCamera"){type = NavType.StringType})
            ) {
                val typeCamera = it.arguments?.getString("typeCamera")
                CameraScreenAddProduct(
                    navigate = {navController.popBackStack()},
                    typeCamera = typeCamera!!,
                    viewModel = addProductViewModel){

                }
            }
            composable(AppScreens.AddProductData.route) {
                AddProductDataScreen(navController, addProductViewModel)
            }
            composable(AppScreens.DetailProduct.route) {
                val scope = rememberCoroutineScope()
                DetailProductScreen(addProductViewModel){
                    scope.launch {
                        addProductViewModel.saveRemoteProduct(context)
                        addProductViewModel.cleanVehicleModel()
                        data.putExtra("complete_add_product", true)
                        activity?.setResult(Activity.RESULT_OK, data)
                        activity?.finish()
                    }
                }
            }
        }
    }
}