package com.nawl.carmaintenanceapp.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nawl.carmaintenanceapp.MainApplication
import com.nawl.carmaintenanceapp.ui.theme.CarMaintenanceAppTheme
import com.nawl.carmaintenanceapp.viewmodel.MaintenanceViewModel
import com.nawl.carmaintenanceapp.viewmodel.MaintenanceViewModelFactory
import com.nawl.carmaintenanceapp.viewmodel.TripViewModel
import com.nawl.carmaintenanceapp.viewmodel.TripViewModelFactory

@Composable
fun TripLogsScreen() {

    val context = LocalContext.current
    val application = context.applicationContext as MainApplication
    val tripViewModel: TripViewModel = viewModel(
        factory = TripViewModelFactory(application.database.tripLogDao())
    )
    Column(
        modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
    ) {
        CarMaintenanceAppTheme {
            Scaffold(
                floatingActionButton = { AddTripLogPopUpButton(tripViewModel) },
                floatingActionButtonPosition = FabPosition.End
            ) { innerPadding ->
                Text("Trip Logs", modifier = Modifier.padding(innerPadding))
//                MaintenanceLogsList(maintenanceViewModel, Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
fun AddTripLogPopUpButton(
    tripViewModel: TripViewModel
) {
    var showTripLogForm by remember { mutableStateOf(false) }
    FloatingActionButton(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        onClick = {
            showTripLogForm = true
        }) {
        Icon(Icons.Filled.Add, contentDescription = "Add new item")
    }
    if (showTripLogForm) {
        Dialog(onDismissRequest = { showTripLogForm = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface
            ) {
                TripLogForm(onDismiss = { showTripLogForm = false })
            }
        }
    }
}