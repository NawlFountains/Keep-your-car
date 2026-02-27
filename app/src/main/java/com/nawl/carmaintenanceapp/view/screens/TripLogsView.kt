package com.nawl.carmaintenanceapp.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nawl.carmaintenanceapp.MainApplication
import com.nawl.carmaintenanceapp.model.entities.TripLog
import com.nawl.carmaintenanceapp.ui.theme.CarMaintenanceAppTheme
import com.nawl.carmaintenanceapp.view.ConvertToCurrentDistanceUnit
import com.nawl.carmaintenanceapp.view.DISTANCE_UNIT
import com.nawl.carmaintenanceapp.view.formatToUTC
import com.nawl.carmaintenanceapp.viewmodel.TripViewModel
import com.nawl.carmaintenanceapp.viewmodel.MainViewModel
import java.sql.Date

@Composable
fun TripLogsScreen(
    mainViewModel: MainViewModel
) {
    val tripViewModel = mainViewModel.tripViewModel

    Column(
        modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
    ) {
        CarMaintenanceAppTheme {
            Scaffold(
                floatingActionButton = { AddTripLogPopUpButton(tripViewModel) },
                floatingActionButtonPosition = FabPosition.End
            ) { innerPadding ->
                Text("Trip Logs", modifier = Modifier.padding(innerPadding))
                TripLogsList(5,tripViewModel, Modifier.padding(innerPadding))
            }
        }
    }
}
@Composable
fun TripLogsList(maxLatestLogs: Int, tripViewModel: TripViewModel, modifier: Modifier = Modifier) {
    val latestTripLogs by tripViewModel.getLatestTripLogs(maxLatestLogs).collectAsState(initial = emptyList())

    Box(
        Modifier.background(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(16.dp)
        ).fillMaxWidth()
    ) {
        Column(
            modifier = modifier
        ) {
            if (latestTripLogs.isEmpty()) {
                Text("Start adding trip logs!", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                return
            } else {
                Text("Trip logs", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(8.dp)
                ){
                    val modifier = Modifier.padding(8.dp)
                    Text("Origin", modifier = modifier.weight(2f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    Text("Destination", modifier = modifier.weight(2f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    if (DISTANCE_UNIT == "km")
                        Text("Kilometrage", modifier = modifier.weight(2f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    else
                        Text("Mileage", modifier = modifier.weight(2f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)

                    Text("Date", modifier = modifier.weight(2f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    Text("", modifier = modifier.weight(1f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    Text("", modifier = modifier.weight(1f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                }
            }
            latestTripLogs.forEach { tripLog ->
                TripLogEditableCard(tripLog, tripViewModel)
            }
        }
    }
}

@Composable
fun TripLogEditableCard(tripLog: TripLog, tripViewModel: TripViewModel) {
    val modifier = Modifier.padding(8.dp)

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(tripLog.origin, modifier = modifier.weight(2f), color = MaterialTheme.colorScheme.onSecondaryContainer)
        Text(tripLog.destination, modifier = modifier.weight(2f), color = MaterialTheme.colorScheme.onSecondaryContainer)
        Text(ConvertToCurrentDistanceUnit(tripLog.distance).toString() + " " + DISTANCE_UNIT, modifier = modifier.weight(2f), color = MaterialTheme.colorScheme.onSecondaryContainer)
        Text(formatToUTC(tripLog.date), modifier = modifier.weight(2f), color = MaterialTheme.colorScheme.onSecondaryContainer)
        Column(
            modifier =Modifier.weight(1f)
        ) {
            EditTripLogButton(tripLog, tripViewModel)
        }
        Column(
            modifier =Modifier.weight(1f)
        ) {
            DeleteTripLogButton(tripLog, tripViewModel)
        }
    }
}
@Composable
fun DeleteTripLogButton(tripLog: TripLog, tripViewModel: TripViewModel) {
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
                DeleteTripLogConfirmationForm(
                    tripLog = tripLog,
                    tripViewModel = tripViewModel,
                    onDismiss = { showConfirmationForm = false })
            }
        }

    }
}

@Composable
fun DeleteTripLogConfirmationForm(
    tripLog: TripLog,
    tripViewModel: TripViewModel,
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
                    tripViewModel.deleteTripLog(tripLog)
                    onDismiss()
                }) {
                Text("Delete")
            }
        }
    }
}
@Composable
fun EditTripLogButton(tripLog: TripLog, tripViewModel: TripViewModel) {
    var showEditForm by remember { mutableStateOf(false) }

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        ),
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier.fillMaxWidth().height(36.dp),
        onClick = {
            showEditForm = true
        }) {
        Icon(Icons.Filled.Edit, contentDescription = "Edit", modifier = Modifier.fillMaxWidth())
    }
    if (showEditForm) {
        Dialog(onDismissRequest = { showEditForm = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface
            ) {
                TripLogEditableForm(
                    tripLog = tripLog,
                    tripViewModel = tripViewModel,
                    onDismiss = { showEditForm = false })
            }
        }

    }
}

@Composable
fun TripLogEditableForm (
    tripLog: TripLog,
    onDismiss: () -> Unit ,
    tripViewModel: TripViewModel
) {
    val formState by tripViewModel.formState.collectAsState()

    var showDateMenu by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    LaunchedEffect(tripLog) {
        tripViewModel.onOriginChanged(tripLog.origin)
        tripViewModel.onDestinationChanged(tripLog.destination)
        tripViewModel.onDateChanged(tripLog.date)
        tripViewModel.onDistanceChanged(tripLog.distance)
    }


    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Edit Trip log",
            modifier = Modifier.padding(bottom = 16.dp),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge
        )
        TextField(
            value = formState.origin,
            onValueChange = { tripViewModel.onOriginChanged(it) },
            isError = formState.originError != null,
            label = { Text("From") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        formState.originError?.let {
            Text(it, color = Color.Red)
        }
        TextField(
            value = formState.destination,
            onValueChange = { tripViewModel.onDestinationChanged(it)},
            isError = formState.destinationError != null,
            label = { Text("To") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        formState.destinationError?.let {
            Text(it, color = Color.Red)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = formState.distance.toString(),
                onValueChange = { tripViewModel.onDistanceChanged(it.toIntOrNull() ?: 0) },
                isError = formState.distanceError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("Distance") },
                modifier = Modifier.weight(1f)
            )
            Text(
                text = DISTANCE_UNIT,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        formState.distanceError?.let {
            Text(it, color = Color.Red)
        }
        TextField(
            value = formatToUTC(formState.date),
            onValueChange = { },
            label = { Text("Date") },
            readOnly = true,
            isError = formState.dateError != null,
            trailingIcon = {
                IconButton(onClick = { showDateMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .clickable { showDateMenu = true }
        )

        if (showDateMenu) {
            DatePickerDialog(
                onDismissRequest = { showDateMenu = false },
                confirmButton = {
                    TextButton(onClick = {
                        showDateMenu = false
                        tripViewModel.onDateChanged(datePickerState.selectedDateMillis?.let { Date(it) }!!)
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDateMenu = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
        formState.dateError?.let {
            Text(it, color = Color.Red)
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Button(
                modifier = Modifier.padding(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                onClick = {
                    onDismiss()
                    tripViewModel.resetFormState()
                }) {
                Text("Cancel")
            }
            Button(
                modifier = Modifier.padding(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                onClick = {
                    if (tripViewModel.validate()) {
                        tripViewModel.editTripLog(tripLog.id, formState.origin, formState.destination, formState.date, formState.distance)
                        tripViewModel.resetFormState()
                        onDismiss()
                    }
                }) {
                Text("Edit")
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
                TripLogForm(
                    onDismiss = { showTripLogForm = false },
                    tripViewModel = tripViewModel
                )
            }
        }
    }
}