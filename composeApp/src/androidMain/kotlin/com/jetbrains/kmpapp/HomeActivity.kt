package com.jetbrains.kmpapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jetbrains.kmpapp.DI.moduleHome
import com.jetbrains.kmpapp.DI.moduleHomeViewModel
import com.jetbrains.kmpapp.colors.appsBarColor
import com.jetbrains.kmpapp.colors.bottomBarSelectedItemColor
import com.jetbrains.kmpapp.component.appBar.TopAppBar
import com.jetbrains.kmpapp.component.modal.Modal
import com.jetbrains.kmpapp.component.visible
import com.jetbrains.kmpapp.model.BottomBarItemModel
import com.jetbrains.kmpapp.model.BottomBarItemType
import com.jetbrains.kmpapp.navigation.AppScreens
import com.jetbrains.kmpapp.navigation.home.NavigationHome
import com.jetbrains.kmpapp.screens.home.HomeViewModel
import com.jetbrains.kmpapp.utils.crypto.AliasTag
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.loadKoinModules
import java.util.ArrayList

class HomeActivity : ComponentActivity() {
    companion object{
        fun intentProvider(context: Context) = Intent(context, HomeActivity::class.java)
    }

    val viewModel : HomeViewModel by inject()

    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Registra si la actividad cerro correctamente y si contiene datos
        val route = MutableLiveData("")

        val launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                val data : Intent? = result.data
                val resultAddProduct = data?.getBooleanExtra("complete_add_product",false)
                if (resultAddProduct == true){
                    viewModel.cleanUris()
                    route.value = "stock_screen"
                }
            }
            if (result.resultCode == Activity.RESULT_CANCELED){
                val data : Intent? = result.data
                val resultAddProduct = data?.getStringArrayExtra("list_uris")
                if (resultAddProduct != null){
                    val listUris = resultAddProduct.map {
                        it.toUri()
                    }
                    viewModel.replaceUris(listUris)
                }
            }
        }
        val intent = AddProductActivity.intentProvider(context = this@HomeActivity)

        val modules = listOf(moduleHome, moduleHomeViewModel)
        loadKoinModules(
            modules = modules
        )

        setContent {
            KoinAndroidContext {
                val navController = rememberNavController()
                var visibilityBar by remember { mutableStateOf(true) }
                var topAppBarName by remember { mutableStateOf("Home") }
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                var isLogout by remember { mutableStateOf(true) }
                var closeSession by remember { mutableStateOf(false) }
                val sessionStatus = viewModel.session.collectAsState()

                LaunchedEffect(currentRoute) {
                    if (currentRoute != null) {
                         when (currentRoute) {
                            AppScreens.Home.route ->{
                                topAppBarName = AppScreens.Home.title
                                isLogout = true
                            }
                            AppScreens.Stock.route -> {
                                isLogout = false
                                topAppBarName = AppScreens.Stock.title
                            }
                            else ->
                            {
                                isLogout = false
                                topAppBarName = AppScreens.AddProductData.title
                            }
                        }
                    }
                }
                route.observe(this){onChange ->
                    if (onChange.isNotEmpty()){
                        topAppBarName = AppScreens.Stock.title
                        navController.navigate(AppScreens.Stock.route){
                            onNavigateUp()
                            launchSingleTop = true
                        }
                        route.value = ""
                    }
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            titleTopBar = topAppBarName,
                            isLogout = isLogout){
                            if (!visibilityBar) visibilityBar = true
                            if(!isLogout)
                                navController.navigateUp()
                            else
                            {
                                closeSession = true
                            }
                        }
                    },
                    bottomBar = {
                        BottomStackBar(Modifier.visible(visibilityBar), navController){title, isLogoutScreen->
                            topAppBarName = title
                            isLogout = isLogoutScreen
                        }
                    }
                ) {innerPadding->
                    NavigationHome(modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        homeViewModel = viewModel,
                        navigate = { params ->
                            intent.apply {
                                putStringArrayListExtra("uris_selected", ArrayList(params))
                            }
                            launcher.launch(intent)
                        },
                        visibilityBar = { visibilityBar = it }
                    )
                }

                if(closeSession){
                    Modal({ closeSession = false },
                        {
                            val filePath = "${this.filesDir}/${AliasTag.TOKEN.fileName}"
                            viewModel.cleanSession(filePath)
                        },
                        getString(R.string.logout_tittle),
                        getString(R.string.logout_message),
                        "Salir")
                }

                if (sessionStatus.value) {
                    startActivity(MainActivity.intentProvider(this))
                    this@HomeActivity.finish()
                }
            }
        }
    }
}

@Composable
private fun BottomStackBar(modifier: Modifier, navController: NavHostController, topAppBar:(String, Boolean)->Unit) {
    val listBottomBarIcons = listOf(
        BottomBarItemModel("Home", R.drawable.ic_home_item, BottomBarItemType.HOME),
        BottomBarItemModel("Carga", R.drawable.ic_add_product, BottomBarItemType.ADD),
        BottomBarItemModel("Stock", R.drawable.ic_stock_logo, BottomBarItemType.STOCK)
    )
    BottomBar(
        items =  listBottomBarIcons,
        modifier = modifier,
        navController = navController){ index, route, screenName, isLogoutScreen->
        topAppBar(screenName, isLogoutScreen)
        navController.navigate(route){
            launchSingleTop = true
        }
    }
}

@Composable
private fun BottomBar(items: List<BottomBarItemModel>,
                      modifier: Modifier, navController: NavHostController,
                      onChangePosition: (Int, String, String, Boolean) -> Unit,
) {
    var positionSelected by remember { mutableIntStateOf(0) }

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    LaunchedEffect(currentRoute) {
        if (currentRoute != null) {
            positionSelected = when (currentRoute) {
                "home_screen" -> 0
                "stock_screen" -> 2
                else -> 1
            }
        }
    }

    Box(modifier = modifier,contentAlignment = Alignment.BottomCenter){
        NavigationBar(
            containerColor = appsBarColor,
            contentColor = Color.Green,
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(indicatorColor = bottomBarSelectedItemColor),
                    icon = {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(id = item.resource),
                            contentDescription = null
                        )
                    },
                    label = { Text(item.title) },
                    selected = positionSelected == index,
                    onClick = {
                        positionSelected = index
                         when(item.type){
                            BottomBarItemType.HOME -> onChangePosition(index, AppScreens.Home.route, item.title, true)
                            BottomBarItemType.ADD -> onChangePosition(index, AppScreens.OptionTakePhoto.route, item.title, false)
                            BottomBarItemType.STOCK -> onChangePosition(index, AppScreens.Stock.route, item.title, false)
                        }
                    }
                )
            }
        }
    }
}
