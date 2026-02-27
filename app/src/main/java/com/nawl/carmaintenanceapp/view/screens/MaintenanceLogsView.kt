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
import com.nawl.carmaintenanceapp.model.entities.MaintenanceLog
import com.nawl.carmaintenanceapp.ui.theme.CarMaintenanceAppTheme
import com.nawl.carmaintenanceapp.view.ConvertToCurrentDistanceUnit
import com.nawl.carmaintenanceapp.view.DISTANCE_UNIT
import com.nawl.carmaintenanceapp.view.formatToUTC
import com.nawl.carmaintenanceapp.viewmodel.MainViewModel
import com.nawl.carmaintenanceapp.viewmodel.MaintenanceViewModel
import com.nawl.carmaintenanceapp.viewmodel.MaintenanceViewModelFactory
import java.sql.Date

@Composable
fun MaintenanceLogsScreen(
    mainViewModel: MainViewModel
) {
    val maintenanceViewModel = mainViewModel.maintenanceViewModel
    Column(
        modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
    ) {
        CarMaintenanceAppTheme {
            Scaffold(
                floatingActionButton = { AddMaintenanceLogPopUpButton(maintenanceViewModel) },
                floatingActionButtonPosition = FabPosition.End
            ) { innerPadding ->
                MaintenanceLogsList(maintenanceViewModel, Modifier.padding(innerPadding))
            }
        }
    }
}

private var entriesPerScreen = 3


@Composable
fun MaintenanceLogsList(maintenanceViewModel: MaintenanceViewModel, modifier: Modifier = Modifier ) {
    val latestMaintenanceLogs by maintenanceViewModel.getLatestMaintenanceLogs(entriesPerScreen).collectAsState(initial = emptyList())
    Box(
        Modifier.background(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(16.dp)
        ).fillMaxWidth()
    ) {
        Column(
            modifier = modifier
        ) {
            if (latestMaintenanceLogs.isEmpty()) {
                Text("Start adding maintenance logs!", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                return
            } else {
                Text("Maintenance logs", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(8.dp)
                ){
                    val modifier = Modifier.padding(8.dp)
                    Text("Item", modifier = modifier.weight(3f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    if (DISTANCE_UNIT == "km")
                        Text("Kilometrage", modifier = modifier.weight(3f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    else
                        Text("Mileage", modifier = modifier.weight(3f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)

                    Text("Date", modifier = modifier.weight(3f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    Text("", modifier = modifier.weight(1f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    Text("", modifier = modifier.weight(1f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                }
            }
            latestMaintenanceLogs.forEach { maintenanceLog ->
                Row() {
                    MaintenanceLogEditableCard(maintenanceLog, maintenanceViewModel)
                }
            }
        }
    }
}

@Composable
fun MaintenanceLogEditableCard(maintenanceLog: MaintenanceLog, maintenanceViewModel: MaintenanceViewModel) {
    val modifier = Modifier.padding(8.dp)

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(maintenanceLog.itemChanged, modifier = modifier.weight(3f), color = MaterialTheme.colorScheme.onSecondaryContainer)
        Text(ConvertToCurrentDistanceUnit(maintenanceLog.kilometrage).toString() + " " + DISTANCE_UNIT, modifier = modifier.weight(3f), color = MaterialTheme.colorScheme.onSecondaryContainer)
        Text(formatToUTC(maintenanceLog.date), modifier = modifier.weight(3f), color = MaterialTheme.colorScheme.onSecondaryContainer)
        Column( modifier =Modifier.weight(1f)) {
            EditMaintenanceLogButton(maintenanceLog, maintenanceViewModel)
        }
        Column( modifier =Modifier.weight(1f)) {
            DeleteMaintenanceLogButton(maintenanceLog, maintenanceViewModel)
        }
    }
}

@Composable
fun DeleteMaintenanceLogButton(maintenanceLog: MaintenanceLog, maintenanceViewModel: MaintenanceViewModel) {
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
                DeleteMaintenanceLogConfirmationForm(
                    maintenanceLog = maintenanceLog,
                    maintenanceViewModel = maintenanceViewModel,
                    onDismiss = { showConfirmationForm = false })
            }
        }

    }
}

@Composable
fun DeleteMaintenanceLogConfirmationForm(
    maintenanceLog: MaintenanceLog,
    maintenanceViewModel: MaintenanceViewModel,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("Are you sure you want to delete this maintenance log?", textAlign = TextAlign.Center)
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
                    maintenanceViewModel.deleteMaintenanceLog(maintenanceLog)
                    onDismiss()
                }) {
                Text("Delete")
            }
        }
    }
}

@Composable
fun EditMaintenanceLogButton(maintenanceLog: MaintenanceLog, maintenanceViewModel: MaintenanceViewModel) {
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
        Icon(Icons.Filled.Edit, contentDescription = "Delete", modifier = Modifier.fillMaxWidth())
    }
    if (showEditForm) {
        Dialog(onDismissRequest = { showEditForm = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface
            ) {
                MaintenanceLogEditableForm(
                    maintenanceLog = maintenanceLog,
                    maintenanceViewModel = maintenanceViewModel,
                    onDismiss = { showEditForm = false })
            }
        }

    }
}

@Composable
fun MaintenanceLogEditableForm(
    maintenanceLog: MaintenanceLog,
    onDismiss: () -> Unit,
    maintenanceViewModel: MaintenanceViewModel
) {
    var notes by remember { mutableStateOf("") }
    var showDateMenu by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val formState by maintenanceViewModel.formState.collectAsState()

    LaunchedEffect(maintenanceLog) {
        maintenanceViewModel.onItemChanged(maintenanceLog.itemChanged)
        maintenanceViewModel.onDateChanged(maintenanceLog.date)
        maintenanceViewModel.onKilometrageChanged(maintenanceLog.kilometrage)
        notes = maintenanceLog.notes
    }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Edit maintenance Log",
            modifier = Modifier.padding(bottom = 16.dp),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge
        )
        TextField(
            value = formState.itemChanged,
            onValueChange = { maintenanceViewModel.onItemChanged(it) },
            isError = formState.itemChangedError != null,
            label = { Text("Item changed") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        formState.itemChangedError?.let {
            Text(it, color = Color.Red)
        }

        TextField(
            value = formatToUTC(formState.date),
            onValueChange = { },
            label = { Text("Date") },
            readOnly = true,
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
                        maintenanceViewModel.onDateChanged(datePickerState.selectedDateMillis?.let { Date(it) }!!)
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = formState.kilometrage.toString(),
                onValueChange = {
                    maintenanceViewModel.onKilometrageChanged(it.toIntOrNull() ?: 0
                    ) },
                isError = formState.kilometrageError != null,
                label = {
                    if (DISTANCE_UNIT == "km")
                        Text("Kilometrage")
                    else
                        Text("Mileage")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )

            formState.kilometrageError?.let {
                Text(it, color = Color.Red)
            }

            Box {
                Text(
                    text = DISTANCE_UNIT,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
        TextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Notes") },
            modifier = Modifier
                .fillMaxWidth()
        )
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
                    maintenanceViewModel.resetFormState()
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
                    if (maintenanceViewModel.validate()) {
                        maintenanceViewModel.editMaintenanceLog(maintenanceLog.id, formState.itemChanged, formState.date, formState.kilometrage,notes)
                        onDismiss()
                        maintenanceViewModel.resetFormState()
                    }
                }) {
                Text("Log")
            }
        }
    }

}
@Composable
fun AddMaintenanceLogPopUpButton(
    maintenanceViewModel: MaintenanceViewModel
) {
    var showMaintenanceLogForm by remember { mutableStateOf(false) }
    FloatingActionButton(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        onClick = {
            showMaintenanceLogForm = true
        }) {
        Icon(Icons.Filled.Add, contentDescription = "Add new item")
    }
    if (showMaintenanceLogForm) {
        Dialog(onDismissRequest = { showMaintenanceLogForm = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface
            ) {
                MaintenanceLogForm(
                    onDismiss = { showMaintenanceLogForm = false },
                    maintenanceViewModel = maintenanceViewModel,
                )
            }
        }
    }
}