package com.nawl.carmaintenanceapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nawl.carmaintenanceapp.MainApplication
import com.nawl.carmaintenanceapp.model.entities.FuelLog
import com.nawl.carmaintenanceapp.ui.theme.CarMaintenanceAppTheme
import com.nawl.carmaintenanceapp.viewmodel.FuelViewModel
import com.nawl.carmaintenanceapp.viewmodel.FuelViewModelFactory

@Composable
fun FuelLogsScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as MainApplication
    val fuelViewModel: FuelViewModel = viewModel(
        factory = FuelViewModelFactory(application.database.fuelLogDao())
    )

    Column(
        modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
    ) {
        CarMaintenanceAppTheme {
            Scaffold(
                floatingActionButton = { AddFuelLogPopUpButton(fuelViewModel) },
                floatingActionButtonPosition = FabPosition.End
            ) { innerPadding ->
                Text("Fuel Logs", modifier = Modifier.padding(innerPadding))
                LatestFuelLogsCards(4,fuelViewModel, Modifier.padding(innerPadding))
            }
        }
    }
}
private var fuelGridColumns = 5


@Composable
fun FuelLogEditableCard(fuelLog: FuelLog, fuelViewModel: FuelViewModel) {
    val modifier = Modifier.padding(8.dp)

    LazyVerticalGrid(
        columns = GridCells.Fixed(count = fuelGridColumns),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(8.dp)
    ) {
        items(1) {
            Text(fuelLog.stationName, modifier = modifier, color = MaterialTheme.colorScheme.onSecondaryContainer)
        }
        items(1) {
            Text(String.format("%.2f", ConvertToCurrentLiquidUnit(fuelLog.quantity))+" "+LIQUID_UNIT, modifier = modifier, color = MaterialTheme.colorScheme.onSecondaryContainer)
        }
        items(1) {
            if (fuelLog.isTankFull)
                Icon(Icons.Outlined.Check, contentDescription = "Full", tint = MaterialTheme.colorScheme.onSecondaryContainer)
            else
                Icon(Icons.Outlined.Cancel, contentDescription = "Not Full", tint = MaterialTheme.colorScheme.onSecondaryContainer)
        }
        items(1) {
            Text(ConvertToCurrentDistanceUnit(fuelLog.kilometrage).toString()+" "+DISTANCE_UNIT, modifier = modifier, color = MaterialTheme.colorScheme.onSecondaryContainer)
        }
        items(1) {
            Text(formatter.format(fuelLog.date), modifier = modifier, color = MaterialTheme.colorScheme.onSecondaryContainer)
        }
    }
}

@Composable
fun AddFuelLogPopUpButton(
    fuelViewModel: FuelViewModel
) {
    var showFuelLogForm by remember { mutableStateOf(false) }
    FloatingActionButton(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        onClick = {
            showFuelLogForm = true
        }) {
        Icon(Icons.Filled.Add, contentDescription = "Add new item")
    }
    if (showFuelLogForm) {
        Dialog(onDismissRequest = { showFuelLogForm = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface
            ) {
                FuelLogForm(
                    onDismiss = { showFuelLogForm = false },
                    fuelViewModel = fuelViewModel
                )
            }
        }
    }
}