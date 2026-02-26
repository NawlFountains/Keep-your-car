package com.nawl.carmaintenanceapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nawl.carmaintenanceapp.MainApplication
import com.nawl.carmaintenanceapp.model.entities.MaintenanceLog
import com.nawl.carmaintenanceapp.ui.theme.CarMaintenanceAppTheme
import com.nawl.carmaintenanceapp.viewmodel.MaintenanceViewModel
import com.nawl.carmaintenanceapp.viewmodel.MaintenanceViewModelFactory

@Composable
fun MaintenanceLogsScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as MainApplication
    val maintenanceViewModel: MaintenanceViewModel = viewModel(
        factory = MaintenanceViewModelFactory(application.database.maintenanceLogDao())
    )
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

private var maintenanceGridColumns = 11
private var maintenanceGridLineSpan = 3
private var entriesPerScreen = 3


@Composable
fun MaintenanceLogsList(maintenanceViewModel: MaintenanceViewModel, modifier: Modifier = Modifier ) {
    val latestMaintenanceLogs by maintenanceViewModel.getLatestMaintenanceLogs(entriesPerScreen).collectAsState(initial = emptyList())
    Box(
        Modifier.background(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(16.dp)
        )
    ) {
        Column(
            modifier = modifier
        ) {
            if (latestMaintenanceLogs.isEmpty()) {
                Text("Start adding maintenance logs!", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                return
            } else {
                Text("Maintenance logs:", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(count = maintenanceGridColumns),
                    Modifier.padding(8.dp)
                ){
                    val modifier = Modifier.padding(8.dp)
                    items(1, span = { GridItemSpan(maintenanceGridLineSpan) }) {
                        Text("Item", modifier = modifier, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    }
                    items(1, span = { GridItemSpan(maintenanceGridLineSpan) }) {
                        if (DISTANCE_UNIT == "km")
                            Text("Kilometrage", modifier = modifier, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                        else
                            Text("Mileage", modifier = modifier, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)

                    }
                    items(1, span = { GridItemSpan(maintenanceGridLineSpan) }) {
                        Text("Date", modifier = modifier, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    }
                }
            }
            latestMaintenanceLogs.forEach { maintenanceLog ->
                MaintenanceLogEditableCard(maintenanceLog, maintenanceViewModel)
            }
        }
    }
}

@Composable
fun MaintenanceLogEditableCard(maintenanceLog: MaintenanceLog, maintenanceViewModel: MaintenanceViewModel) {
    val modifier = Modifier.padding(8.dp)

    LazyVerticalGrid(
        columns = GridCells.Fixed(count = maintenanceGridColumns),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(8.dp)
    ) {
        items(1, span = { GridItemSpan(maintenanceGridLineSpan ) }) {
            Text(maintenanceLog.itemChanged, modifier = modifier)
        }
        items(count=1, span = { GridItemSpan(maintenanceGridLineSpan) }) {
            Text(ConvertToCurrentDistanceUnit(maintenanceLog.kilometrage).toString() + " " + DISTANCE_UNIT, modifier = modifier)
        }
        items(count=1, span = { GridItemSpan(maintenanceGridLineSpan) }) {
            Text(formatter.format(maintenanceLog.date), modifier = modifier)
        }
        items(count=1, span = { GridItemSpan(2) }) {
            DeleteMaintenanceLogButton(maintenanceLog, maintenanceViewModel)
        }
    }
}

@Composable
fun DeleteMaintenanceLogButton(maintenanceLog: MaintenanceLog, maintenanceViewModel: MaintenanceViewModel) {
    var showConfirmationForm by remember { mutableStateOf(false) }

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onError,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        ),
        modifier = Modifier.padding(4.dp),
        onClick = {
            showConfirmationForm = true
        }) {
        Icon(Icons.Filled.Delete, contentDescription = "Delete")
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
                    maintenanceViewModel = maintenanceViewModel
                )
            }
        }
    }
}