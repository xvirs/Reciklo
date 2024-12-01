package com.jetbrains.kmpapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jetbrains.kmpapp.DI.moduleAddProduct
import com.jetbrains.kmpapp.DI.moduleAddProductViewModel
import com.jetbrains.kmpapp.component.appBar.TopAppBar
import com.jetbrains.kmpapp.component.modal.Modal
import com.jetbrains.kmpapp.navigation.AppScreens
import com.jetbrains.kmpapp.navigation.addProduct.NavigationAddProduct
import com.jetbrains.kmpapp.screens.addProduct.AddProductViewModel
import com.jetbrains.kmpapp.screens.home.findActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.loadKoinModules

class AddProductActivity : ComponentActivity() {

    companion object{
        fun intentProvider(context: Context) = Intent(context, AddProductActivity::class.java)
    }


    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val listUris = intent.extras?.getStringArrayList("uris_selected")?.map {
            it.toUri()
        }

        val modules = listOf(moduleAddProduct, moduleAddProductViewModel)
        loadKoinModules(
            modules = modules
        )

        val viewModel : AddProductViewModel by inject()

        listUris?.let {
            viewModel.replaceUris(it)
        }
        setContent {
            KoinAndroidContext {
                val navController = rememberNavController()
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                var topAppBarName by remember { mutableStateOf(getString(R.string.top_app_bar_name_add_product)) }
                var isLogout by remember { mutableStateOf(false) }
                val activity = this.findActivity()
                val data = Intent()

                LaunchedEffect(currentRoute){
                    if (currentRoute != null) {
                        when(currentRoute){
                            AppScreens.AddProductData.route -> {topAppBarName =  getString(R.string.top_app_bar_name_add_product)}
                            AppScreens.DetailProduct.route -> topAppBarName = getString(R.string.top_app_bar_name_review_data)
                            AppScreens.CameraScreen.route->topAppBarName = getString(R.string.top_app_bar_name_camera)
                        }
                    }
                }

                if (isLogout) {
                    Modal(
                        {
                            isLogout = false
                        },
                        onConfirmation = {
                            isLogout= false
                            val arrayUris = viewModel.urisPhotos.value.map {
                                it.toString()
                            }.toTypedArray()
                            data.putExtra("list_uris", arrayUris)
                            activity?.setResult(RESULT_CANCELED, data )
                            activity?.finish()
                        },
                        dialogTitle = getString(R.string.logout_add_product_tittle)
                    )
                }

                Scaffold(topBar ={
                    TopAppBar(topAppBarName,
                        false){
                        val nav = navController.navigateUp()
                        if(!nav)
                            isLogout = true
                    }
                }){paddingValues ->

                    Box(modifier = Modifier.fillMaxSize().padding(paddingValues)){
                        NavigationAddProduct(navController, viewModel)
                    }
                }

                BackHandler {
                    isLogout = true
                }
            }
        }
    }
}
