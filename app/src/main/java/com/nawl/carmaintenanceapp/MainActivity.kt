package com.nawl.carmaintenanceapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.nawl.carmaintenanceapp.ui.theme.CarMaintenanceAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarMaintenanceAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Scaffold(
                        floatingActionButton = { PopUpButton() },
                        floatingActionButtonPosition = FabPosition.End
                    ) { innerPadding ->
                        EntryCards(
                            "Title",
                            listOf(
                                "First card",
                                "Second card",
                                "Third card"
                            ),
                            Modifier.padding(innerPadding)
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun EntryCards(title: String, entryCards: List<String>, modifier: Modifier = Modifier) {
    Column (modifier = modifier.padding(16.dp)) {
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
            EntryCards("Title",entryList)
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
fun PopUpButton () {
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
            //TODO maybe refactor for adding new options
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
                MaintenanceLogForm(onDismiss = { showMaintenanceLogForm = false })
            }
        }
    }
    if (showFuelLogForm) {
        Dialog(onDismissRequest = { showFuelLogForm = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface
            ) {
                FuelLogForm(onDismiss = { showFuelLogForm = false })
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
    PopUpButton()
}

@Preview
@Composable
fun TripLogFormPreview() {
    CarMaintenanceAppTheme {
        TripLogForm(onDismiss = {})
    }
}

@Preview
@Composable
fun FuelLogFormPreview() {
    CarMaintenanceAppTheme {
        FuelLogForm(onDismiss = {})
    }
}

@Preview
@Composable
fun MaintenanceLogFormPreview() {
    CarMaintenanceAppTheme {
        MaintenanceLogForm(onDismiss = {})
    }
}

@Composable
fun TripLogForm(onDismiss: () -> Unit) {
    var startLocation by remember { mutableStateOf("") }
    var endLocation by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var distance by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("km") }
    var showUnitMenu by remember { mutableStateOf(false) }

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

            Box {
                Text(
                    text = unit,
                    modifier = Modifier
                        .clickable { showUnitMenu = true }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                DropdownMenu(
                    expanded = showUnitMenu,
                    onDismissRequest = { showUnitMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("km") },
                        onClick = {
                            unit = "km"
                            showUnitMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("miles") },
                        onClick = {
                            unit = "miles"
                            showUnitMenu = false
                        }
                    )
                }
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
            Button( modifier = Modifier.padding(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                onClick = { onDismiss() }) {
                Text("Cancel")
            }
            Button( modifier = Modifier.padding(12.dp),
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

}

@Composable
fun FuelLogForm(onDismiss: () -> Unit) {
    var stationName by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var isTankFull by remember { mutableStateOf(false) }
    var mileage by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("liter") }
    var showUnitMenu by remember { mutableStateOf(false) }

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
            value = date,
            onValueChange = { date = it },
            label = { Text("Date") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        TextField(
            value = stationName,
            onValueChange = { stationName = it },
            label = { Text("Station Name") },
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
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantity") },
                modifier = Modifier.weight(1f)
            )

            Box {
                Text(
                    text = unit,
                    modifier = Modifier
                        .clickable { showUnitMenu = true }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                DropdownMenu(
                    expanded = showUnitMenu,
                    onDismissRequest = { showUnitMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("liter") },
                        onClick = {
                            unit = "liter"
                            showUnitMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("gallons") },
                        onClick = {
                            unit = "gallons"
                            showUnitMenu = false
                        }
                    )
                }
            }

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
        TextField(
            value = mileage,
            onValueChange = { mileage = it },
            label = { Text("Mileage") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
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
            Button( modifier = Modifier.padding(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                onClick = { onDismiss() }) {
                Text("Cancel")
            }
            Button( modifier = Modifier.padding(12.dp),
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
}
@Composable
fun MaintenanceLogForm(onDismiss: () -> Unit) {
    var itemChanged by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var mileage by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("km") }
    var showUnitMenu by remember { mutableStateOf(false) }

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
            value = itemChanged,
            onValueChange = { itemChanged = it },
            label = { Text("Item changed") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        TextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date") },
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
                value = mileage,
                onValueChange = { mileage = it },
                label = { Text("Mileage") },
                modifier = Modifier.weight(1f)
            )

            Box {
                Text(
                    text = unit,
                    modifier = Modifier
                        .clickable { showUnitMenu = true }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                DropdownMenu(
                    expanded = showUnitMenu,
                    onDismissRequest = { showUnitMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("km") },
                        onClick = {
                            unit = "km"
                            showUnitMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("miles") },
                        onClick = {
                            unit = "miles"
                            showUnitMenu = false
                        }
                    )
                }
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
            Button( modifier = Modifier.padding(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                onClick = { onDismiss() }) {
                Text("Cancel")
            }
            Button( modifier = Modifier.padding(12.dp),
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
}
