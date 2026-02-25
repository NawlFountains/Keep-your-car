package com.nawl.carmaintenanceapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CarRepair
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nawl.carmaintenanceapp.MainApplication
import com.nawl.carmaintenanceapp.ui.theme.CarMaintenanceAppTheme
import com.nawl.carmaintenanceapp.viewmodel.FuelViewModel
import com.nawl.carmaintenanceapp.viewmodel.FuelViewModelFactory
import com.nawl.carmaintenanceapp.viewmodel.MaintenanceViewModel
import com.nawl.carmaintenanceapp.viewmodel.MaintenanceViewModelFactory
import com.nawl.carmaintenanceapp.viewmodel.TripViewModel
import com.nawl.carmaintenanceapp.viewmodel.TripViewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val maintenanceViewModel: MaintenanceViewModel by viewModels {
        MaintenanceViewModelFactory((application as MainApplication).database.maintenanceLogDao())
    }
    private val tripViewModel: TripViewModel by viewModels {
        TripViewModelFactory((application as MainApplication).database.tripLogDao())
    }
    private val fuelViewModel: FuelViewModel by viewModels {
        FuelViewModelFactory((application as MainApplication).database.fuelLogDao())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            CarMaintenanceAppTheme {
                NavBar(maintenanceViewModel, tripViewModel, fuelViewModel)
            }
        }
    }
}
sealed class Screen(
    val route: String,
    val icon: ImageVector
) {
    object Home : Screen("home", Icons.Default.Home)
    object MaintenanceLogs : Screen("maintenance logs", Icons.Default.CarRepair)
    object TripLogs : Screen("trip logs", Icons.Default.Map)
    object FuelLogs : Screen("fuel logs", Icons.Default.LocalGasStation)
}

var formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//TODO fix timezone showing a day before

@Composable
fun NavBar(
    maintenanceViewModel: MaintenanceViewModel,
    tripViewModel: TripViewModel,
    fuelViewModel: FuelViewModel
){
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
        }
    ) {
        innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(maintenanceViewModel, tripViewModel, fuelViewModel)
            }
            composable(Screen.MaintenanceLogs.route) {
                MaintenanceLogsScreen(maintenanceViewModel)
            }
            composable(Screen.TripLogs.route) {
                TripLogsScreen(tripViewModel)
            }
            composable(Screen.FuelLogs.route) {
                FuelLogsScreen(fuelViewModel)
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        Screen.Home,
        Screen.MaintenanceLogs,
        Screen.TripLogs,
        Screen.FuelLogs
    )

    NavigationBar {

        val currentRoute =
            navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { screen ->

            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.route
                    )
                }
            )
        }
    }
}