package com.jetbrains.kmpapp.screens.home

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.jetbrains.kmpapp.component.homeScreen.Dashboard
import com.jetbrains.kmpapp.component.homeScreen.LastSaved
import com.jetbrains.kmpapp.component.modal.Modal
import com.jetbrains.kmpapp.domain.models.LatestProductData
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.screens.stock.StockViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel
) {
    val stockViewModel = koinViewModel<StockViewModel>()
    val context = LocalContext.current
    val activity = context.findActivity()
    val dialog = viewModel.showDialog.collectAsState()
    val listLatestProductSaved = viewModel.latestProduct.collectAsState().value
    val profileData = viewModel.profileData.collectAsState()
    var latestProducts by remember { mutableStateOf(LatestProductData(listOf())) }

    LaunchedEffect(Unit){
        viewModel.initHome()
    }

    when(listLatestProductSaved){
        is StatusResult.Error -> {}
        is StatusResult.Success -> {
            latestProducts = listLatestProductSaved.value
        }
    }



    if (dialog.value) {
        Modal(
            onDismissRequest = { viewModel.setToggleDialog() },
            onConfirmation = { activity?.finishAffinity() },
            dialogTitle = "¿Querés salir?",
            onConfirmButtonText = "Salir",
            onDismissButtonText = "Continuar"
        )
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Dashboard(navController, profileData.value)
        LastSaved(latestProducts, stockViewModel)
    }

    BackHandler(onBack = {
        activity?.finish()
    })
}

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}


