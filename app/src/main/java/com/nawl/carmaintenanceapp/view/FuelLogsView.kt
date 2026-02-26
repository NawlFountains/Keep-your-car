package com.nawl.carmaintenanceapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
                FuelLogsList(fuelViewModel, Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
fun FuelLogsList(fuelViewModel: FuelViewModel, modifier: Modifier = Modifier) {
    val latestFuelLogs by fuelViewModel.getLatestFuelLogs(4).collectAsState(initial = emptyList())

    Box(
        Modifier.background(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(16.dp)
        ).fillMaxWidth()
    ) {
        Column(
            modifier = modifier
        ) {
            if (latestFuelLogs.isEmpty()) {
                Text("Start adding Fuel logs!", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                return
            } else {
                Text("Fuel logs", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(8.dp)
                ){
                    val modifier = Modifier.padding(8.dp)
                    Text("Station name", modifier = modifier.weight(2f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    if (LIQUID_UNIT == "l")
                        Text("Litres", modifier = modifier.weight(2f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    else
                        Text("Gallons", modifier = modifier.weight(2f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    if (DISTANCE_UNIT == "km")
                        Text("Kilometrage", modifier = modifier.weight(2f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    else
                        Text("Miles", modifier = modifier.weight(2f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    Text("Date", modifier = modifier.weight(2f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    Text("Full", modifier = modifier.weight(1f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    Text("", modifier = modifier.weight(1f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                }
            }
            latestFuelLogs.forEach { fuelLog ->
                FuelLogEditableCard(fuelLog, fuelViewModel)
            }
        }
    }
}

@Composable
fun FuelLogEditableCard(fuelLog: FuelLog, fuelViewModel: FuelViewModel) {
    val modifier = Modifier.padding(8.dp)

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(fuelLog.stationName, modifier = modifier.weight(2f), color = MaterialTheme.colorScheme.onSecondaryContainer)
        Text(String.format("%.2f", ConvertToCurrentLiquidUnit(fuelLog.quantity))+" "+LIQUID_UNIT, modifier = modifier.weight(2f), color = MaterialTheme.colorScheme.onSecondaryContainer)
        Text(ConvertToCurrentDistanceUnit(fuelLog.kilometrage).toString()+" "+DISTANCE_UNIT, modifier = modifier.weight(2f), color = MaterialTheme.colorScheme.onSecondaryContainer)
        Text(formatToUTC(fuelLog.date), modifier = modifier.weight(2f), color = MaterialTheme.colorScheme.onSecondaryContainer)
        if (fuelLog.isTankFull)
            Icon(Icons.Outlined.Check, modifier = modifier.weight(1f), contentDescription = "Full", tint = MaterialTheme.colorScheme.onSecondaryContainer)
        else
            Icon(Icons.Outlined.Cancel, modifier = modifier.weight(1f), contentDescription = "Not Full", tint = MaterialTheme.colorScheme.onSecondaryContainer)
        Column( modifier =Modifier.weight(1f)) {
            DeleteFuelLogButton(fuelLog, fuelViewModel)
        }
    }
}

@Composable
fun DeleteFuelLogButton(fuelLog: FuelLog, fuelViewModel: FuelViewModel) {
    var showConfirmationForm by remember { mutableStateOf(false) }

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        ),
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier.fillMaxWidth().height(36.dp),
        onClick = {
            showConfirmationForm = true
        }) {
        Icon(Icons.Filled.Delete, contentDescription = "Delete", modifier = Modifier.fillMaxWidth())
    }
    if (showConfirmationForm) {
        Dialog(onDismissRequest = { showConfirmationForm = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface
            ) {
                DeleteFuelLogConfirmationForm(
                    fuelLog = fuelLog,
                    fuelViewModel = fuelViewModel,
                    onDismiss = { showConfirmationForm = false })
            }
        }

    }
}

@Composable
fun DeleteFuelLogConfirmationForm(
    fuelLog: FuelLog,
    fuelViewModel: FuelViewModel,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("Are you sure you want to delete this fuel log?", textAlign = TextAlign.Center)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Button(
                modifier = Modifier.padding(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                onClick = { onDismiss() }) {
                Text("Cancel")
            }
            Button(
                modifier = Modifier.padding(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onError,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                onClick = {
                    fuelViewModel.deleteFuelLog(fuelLog)
                    onDismiss()
                }) {
                Text("Delete")
            }
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