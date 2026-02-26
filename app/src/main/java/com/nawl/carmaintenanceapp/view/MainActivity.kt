package com.nawl.carmaintenanceapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import com.nawl.carmaintenanceapp.ui.theme.CarMaintenanceAppTheme
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            CarMaintenanceAppTheme {
                NavBar()
            }
        }
    }
}

//TODO add to settings
var DISTANCE_UNIT = "km"
var LIQUID_UNIT = "l"
var DATE_FORMAT = "dd/MM/yyyy"

fun ConvertToCurrentDistanceUnit(value: Int): Int {
    var distanceToReturn: Int = value
    if (DISTANCE_UNIT == "mi") {
        distanceToReturn = (value * 0.621371).roundToInt()
    }
    return distanceToReturn
}

fun ConvertToCurrentLiquidUnit(value: Float): Float {
    var liquidToReturn: Float = value
    if (LIQUID_UNIT == "gal") {
        liquidToReturn = (value * 0.264172).toFloat()
    }
    return liquidToReturn
}
fun ConvertToMetricDistanceUnit(value: Int): Int {
    var distanceToReturn: Int = value
    if (DISTANCE_UNIT == "mi") {
        distanceToReturn = (value * 1.609344).roundToInt()
    }
    return distanceToReturn
}

fun ConvertToMetricLiquidUnit(value: Float): Float {
    var liquidToReturn: Float = value
    if (LIQUID_UNIT == "gal") {
        liquidToReturn = (value * 3.785411784).toFloat()
    }
    return liquidToReturn
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

var formatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
//TODO fix timezone showing a day before

@Composable
fun NavBar(){
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
                HomeScreen()
            }
            composable(Screen.MaintenanceLogs.route) {
                MaintenanceLogsScreen()
            }
            composable(Screen.TripLogs.route) {
                TripLogsScreen()
            }
            composable(Screen.FuelLogs.route) {
                FuelLogsScreen()
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