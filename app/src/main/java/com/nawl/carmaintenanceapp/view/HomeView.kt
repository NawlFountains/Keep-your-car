package com.nawl.carmaintenanceapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nawl.carmaintenanceapp.MainApplication
import com.nawl.carmaintenanceapp.model.entities.FuelLog
import com.nawl.carmaintenanceapp.model.entities.MaintenanceLog
import com.nawl.carmaintenanceapp.ui.theme.CarMaintenanceAppTheme
import com.nawl.carmaintenanceapp.viewmodel.FuelViewModel
import com.nawl.carmaintenanceapp.viewmodel.FuelViewModelFactory
import com.nawl.carmaintenanceapp.viewmodel.MaintenanceViewModel
import com.nawl.carmaintenanceapp.viewmodel.MaintenanceViewModelFactory
import com.nawl.carmaintenanceapp.viewmodel.TripViewModel
import com.nawl.carmaintenanceapp.viewmodel.TripViewModelFactory
import java.sql.Date
import kotlin.collections.forEach

@Composable
fun HomeScreen(
) {
    val context = LocalContext.current
    val application = context.applicationContext as MainApplication
    val maintenanceViewModel: MaintenanceViewModel = viewModel(
        factory = MaintenanceViewModelFactory(application.database.maintenanceLogDao())
    )
    val tripViewModel: TripViewModel = viewModel(
        factory = TripViewModelFactory(application.database.tripLogDao())
    )
    val fuelViewModel: FuelViewModel = viewModel(
        factory = FuelViewModelFactory(application.database.fuelLogDao())
    )


    Column(
        modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
    ) {
        CarMaintenanceAppTheme {
                Surface() {
                    Scaffold(
                        floatingActionButton = { MultiOptionPopUpButton(maintenanceViewModel, tripViewModel, fuelViewModel) },
                        floatingActionButtonPosition = FabPosition.End
                    ) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(16.dp)


                        ) {
                            LatestMaintenanceLogsCards(5, maintenanceViewModel, Modifier.padding(innerPadding))
                            LatestFuelLogsCards(5, fuelViewModel, Modifier.padding(innerPadding))
                        }
                    }
                }
        }
    }
}

private var maintenanceGridColumns = 3
private var fuelGridColumns = 5

@Composable
fun LatestMaintenanceLogsCards(maxLatestLogs: Int, maintenanceViewModel: MaintenanceViewModel, modifier: Modifier = Modifier) {
    val latestMaintenanceLogs by maintenanceViewModel.getLatestMaintenanceLogs(maxLatestLogs).collectAsState(initial = emptyList())

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
                Text("Latest maintenance logs", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                Row(
                    Modifier.padding(8.dp)
                ){
                    val modifier = Modifier.padding(8.dp)
                    Text("Item", modifier = modifier.weight(1f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    if (DISTANCE_UNIT == "km")
                        Text("Kilometrage", modifier = modifier.weight(1f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    else
                        Text("Mileage", modifier = modifier.weight(1f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)

                    Text("Date", modifier = modifier.weight(1f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)

                }
            }
            latestMaintenanceLogs.forEach { maintenanceLog ->
                Row() {
                    MaintenanceLogCard(maintenanceLog)
                }
            }
        }
    }
}

@Preview
@Composable
fun LatestMaintenanceLogsCardsPreview() {
    val latestMaintenanceLogs = dummyLatestMaintenanceLogs()

    Column(modifier = Modifier.padding(2.dp)) {
        if (latestMaintenanceLogs.isEmpty()) {
            Text("Start adding maintenance logs!", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
            return
        } else {
            Text("Latest maintenance logs:", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
            LazyVerticalGrid(
                columns = GridCells.Fixed(count = maintenanceGridColumns),
                Modifier.padding(8.dp)
            ){
                val modifier = Modifier.padding(8.dp)
                items(1) {
                    Text("Item", modifier = modifier, style = MaterialTheme.typography.titleMedium)
                }
                items(1) {
                    Text("Kilometrage", modifier = modifier, style = MaterialTheme.typography.titleMedium)
                }
                items(1) {
                    Text("Date", modifier = modifier, style = MaterialTheme.typography.titleMedium)
                }

            }
        }
        latestMaintenanceLogs.forEach { maintenanceLog ->
//            MaintenanceLogCard(maintenanceLog, null)
        }
    }
}

fun dummyLatestMaintenanceLogs(): List<MaintenanceLog> {
    val maintenanceLogs = listOf(
        MaintenanceLog(
            itemChanged = "Battery",
            date = Date(2023, 1, 1),
            kilometrage = 1633,
            notes = "No notes"
        ),
        MaintenanceLog(
            itemChanged = "Washer pump",
            date = Date(2023, 1, 1),
            kilometrage = 851923,
            notes = "No notes"
        ),
        MaintenanceLog(
            itemChanged = "Engine",
            date = Date(2023, 1, 1),
            kilometrage = 1633,
            notes = "No notes"
        ))
    return maintenanceLogs
}

@Composable
fun MaintenanceLogCard(maintenanceLog: MaintenanceLog) {
    val modifier = Modifier.padding(8.dp)

    Row(
    ) {
        Text(maintenanceLog.itemChanged, modifier = modifier.weight(1f), color = MaterialTheme.colorScheme.onSecondaryContainer)
        Text(ConvertToCurrentDistanceUnit(maintenanceLog.kilometrage).toString() + " " + DISTANCE_UNIT, modifier = modifier.weight(1f), color = MaterialTheme.colorScheme.onSecondaryContainer)
        Text(formatter.format(maintenanceLog.date), modifier = modifier.weight(1f), color = MaterialTheme.colorScheme.onSecondaryContainer)
        }
}


@Preview
@Composable
fun MaintenanceLogCardPreview() {

}

@Preview
@Composable
fun PreviewMaintenanceLogCard() {
//    MaintenanceLogCard(
//        MaintenanceLog(
//            itemChanged = "Battery",
//            date = Date(2023, 1, 1),
//            mileage = 1633,
//            unit = "km",
//            notes = "No notes"
//        )
//    )
}

@Composable
fun EntryCards(title: String, entryCards: List<String>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        TitleEntryCard(title, Modifier.padding())
        entryCards.forEach { text ->
            EntryCard(text, Modifier.padding(vertical = 4.dp, horizontal = 12.dp))
        }
    }
}

@Preview
@Composable
fun PreviewEntryCards() {
    CarMaintenanceAppTheme() {
        Surface {
            val entryList = listOf("First card", "Second card", "Third card")
            EntryCards("Title", entryList)
        }
    }
}

@Composable
fun EntryCard(text: String, modifier: Modifier = Modifier) {
    Text(text, modifier, color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.bodyMedium)
}

@Composable
fun TitleEntryCard(text: String, modifier: Modifier = Modifier) {
    Text(text, modifier, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleLarge)
}

@Preview
@Composable
fun PreviewEntryCard() {
    EntryCard("Testing right here right now")
}

@Composable
fun MultiOptionPopUpButton(
    maintenanceViewModel: MaintenanceViewModel,
    tripViewModel: TripViewModel,
    fuelViewModel: FuelViewModel
) {
    var showMenu by remember { mutableStateOf(false) }
    var showMaintenanceLogForm by remember { mutableStateOf(false) }
    var showFuelLogForm by remember { mutableStateOf(false) }
    var showTripLogForm by remember { mutableStateOf(false) }

    Box {
        FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            onClick = {
                showMenu = true
            }) {
            Icon(Icons.Filled.Add, contentDescription = "Add new item")
        }

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                text = { Text("Add Maintenance Log") },
                onClick = {
                    showMenu = false
                    showMaintenanceLogForm = true
                }
            )
            DropdownMenuItem(
                text = { Text("Add Fuel Log") },
                onClick = {
                    showMenu = false
                    showFuelLogForm = true
                }
            )
            DropdownMenuItem(
                text = { Text("Add Trip Log") },
                onClick = {
                    showMenu = false
                    showTripLogForm = true
                }
            )
        }
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

@Preview
@Composable
fun PreviewPopUpButton() {
//    PopUpButton(null, null)
}

@Preview
@Composable
fun TripLogFormPreview() {
    CarMaintenanceAppTheme {
        TripLogForm(onDismiss = {})
    }
}


@Composable
fun LatestFuelLogsCards(maxLatestLogs: Int, fuelViewModel: FuelViewModel, modifier: Modifier = Modifier) {
    val latestFuelLogs by fuelViewModel.getLatestFuelLogs(maxLatestLogs).collectAsState(initial = emptyList())

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
                Text("Latest Fuel logs", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
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
                }
            }
            latestFuelLogs.forEach { fuelLog ->
                FuelLogCard(fuelLog)
            }
        }
    }
}

@Composable
fun FuelLogCard(fuelLog: FuelLog) {
    val modifier = Modifier.padding(8.dp)

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(fuelLog.stationName, modifier = modifier.weight(2f), color = MaterialTheme.colorScheme.onSecondaryContainer)
        Text(String.format("%.2f", ConvertToCurrentLiquidUnit(fuelLog.quantity))+" "+LIQUID_UNIT, modifier = modifier.weight(2f), color = MaterialTheme.colorScheme.onSecondaryContainer)
        Text(ConvertToCurrentDistanceUnit(fuelLog.kilometrage).toString()+" "+DISTANCE_UNIT, modifier = modifier.weight(2f), color = MaterialTheme.colorScheme.onSecondaryContainer)
        Text(formatter.format(fuelLog.date), modifier = modifier.weight(2f), color = MaterialTheme.colorScheme.onSecondaryContainer)
        if (fuelLog.isTankFull)
            Icon(Icons.Outlined.Check, modifier = modifier.weight(1f), contentDescription = "Full", tint = MaterialTheme.colorScheme.onSecondaryContainer)
        else
            Icon(Icons.Outlined.Cancel, modifier = modifier.weight(1f), contentDescription = "Not Full", tint = MaterialTheme.colorScheme.onSecondaryContainer)
    }
}


@Composable
fun TripLogForm(onDismiss: () -> Unit) {

    var startLocation by remember { mutableStateOf("") }
    var endLocation by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var distance by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Trip log",
            modifier = Modifier.padding(bottom = 16.dp),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge
        )
        TextField(
            value = startLocation,
            onValueChange = { startLocation = it },
            label = { Text("From") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        TextField(
            value = endLocation,
            onValueChange = { endLocation = it },
            label = { Text("To") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = distance,
                onValueChange = { distance = it },
                label = { Text("Distance") },
                modifier = Modifier.weight(1f)
            )
            Text(
                text = DISTANCE_UNIT ,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            }
        }
        TextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
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
                onClick = { onDismiss() }) {
                Text("Cancel")
            }
            Button(
                modifier = Modifier.padding(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                onClick = {
                    /*TODO*/
                    onDismiss()
                }) {
                Text("Log")
            }
        }
    }


@Composable
fun FuelLogForm(
    onDismiss: () -> Unit,
    fuelViewModel: FuelViewModel
) {
    val formState by fuelViewModel.formState.collectAsState()


    var isTankFull by remember { mutableStateOf(false) }
    var notes by remember { mutableStateOf("") }
    var showDateMenu by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()


    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Fuel Log",
            modifier = Modifier.padding(bottom = 16.dp),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge
        )
        TextField(
            value = formatter.format(formState.date),
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
                        fuelViewModel.onDateChanged(datePickerState.selectedDateMillis?.let { Date(it) }!!)
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
        TextField(
            value = formState.stationName,
            onValueChange = { fuelViewModel.onStationNameChanged(it) },
            label = { Text("Station Name") },
            isError = formState.stationNameError != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        formState.stationNameError?.let {
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
                value = String.format("%.2f", ConvertToCurrentLiquidUnit(formState.quantity)),
                onValueChange = { fuelViewModel.onQuantityChanged(it.toFloat()) },
                label = { Text("Quantity") },
                modifier = Modifier.weight(1f)
            )
            formState.quantityError?.let {
                Text(it, color = Color.Red)
            }

            Text(
                text = LIQUID_UNIT,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { isTankFull = !isTankFull }
            ) {
                RadioButton(
                    selected = isTankFull,
                    onClick = { isTankFull = !isTankFull }
                )
                Text("Full")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = ConvertToCurrentDistanceUnit(formState.kilometrage).toString(),
                onValueChange = {
                    fuelViewModel.onKilometrageChanged(it.toIntOrNull() ?: 0)
                     },
                label = {
                    if (DISTANCE_UNIT == "km")
                        Text("Kilometrage")
                    else
                        Text("Mileage")
                        },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = DISTANCE_UNIT,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        TextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Notes") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
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
                    fuelViewModel.resetFormState()
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
                    /*TODO*/
                    if (fuelViewModel.validate()) {
                        fuelViewModel.addFuelLog(formState.stationName, formState.quantity, isTankFull, formState.date, formState.kilometrage, notes)
                        onDismiss()
                        fuelViewModel.resetFormState()
                    }
                }) {
                Text("Log")
            }
        }
    }
}

@Composable
fun MaintenanceLogForm(
    onDismiss: () -> Unit,
    maintenanceViewModel: MaintenanceViewModel
) {
    var notes by remember { mutableStateOf("") }
    var showDateMenu by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val formState by maintenanceViewModel.formState.collectAsState()



    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Maintenance Log",
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
            value = formatter.format(formState.date),
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
                        maintenanceViewModel.addMaintenanceLog(formState.itemChanged, formState.date, formState.kilometrage,  notes)
                        onDismiss()
                        maintenanceViewModel.resetFormState()
                    }
                }) {
                Text("Log")
            }
        }
    }

}