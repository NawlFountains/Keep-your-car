package com.nawl.carmaintenanceapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nawl.carmaintenanceapp.model.entities.FuelLog
import com.nawl.carmaintenanceapp.model.entities.MaintenanceLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.sql.Date
import kotlinx.coroutines.flow.flatMapLatest

class HomeViewModel(
    val maintenanceViewModel: MaintenanceViewModel,
    val tripViewModel: TripViewModel,
    val fuelViewModel: FuelViewModel
): ViewModel() {
    var currentKilometrage: StateFlow<Int> = calculateCurrentKilometrage()

    private fun calculateCurrentKilometrage(): StateFlow<Int> {
        val latestMaintenanceLogFlow: Flow<List<MaintenanceLog>> = maintenanceViewModel.getLatestMaintenanceLogs(1)
        val latestFuelLogFlow: Flow<List<FuelLog>> = fuelViewModel.getLatestFuelLogs(1)

        // --- STEP 1: Find the most recent log (Maintenance or Fuel) ---
        // This flow will emit a Pair containing the base kilometrage and the date of that log.
        val mostRecentLogDataFlow: Flow<Pair<Int, Date>?> = combine(
            latestMaintenanceLogFlow,
            latestFuelLogFlow
        ) { maintenanceList, fuelList ->
            val lastMaint = maintenanceList.firstOrNull()
            val lastFuel = fuelList.firstOrNull()

            when {
                lastMaint != null && lastFuel != null -> {
                    if (lastMaint.date >= lastFuel.date) {
                        lastMaint.kilometrage to lastMaint.date // Return a Pair of (Kilometrage, Date)
                    } else {
                        lastFuel.kilometrage to lastFuel.date
                    }
                }

                lastMaint != null -> lastMaint.kilometrage to lastMaint.date
                lastFuel != null -> lastFuel.kilometrage to lastFuel.date
                else -> null // No logs exist at all
            }
        }

        // --- STEP 2: Use flatMapLatest to combine the base KM with subsequent trips ---
        // `flatMapLatest` gets the emission from the flow above (the Pair) and, based on that,
        // creates and switches to a new flow.
        return mostRecentLogDataFlow.flatMapLatest { mostRecentLogData ->
            val baseKilometrage = mostRecentLogData?.first ?: 0
            val lastLogDate = mostRecentLogData?.second ?: Date(0) // Use epoch start if no logs exist

            Log.d("DashboardVM", "Base KM is $baseKilometrage from date $lastLogDate")

            // Get a flow of all trips that happened between the last log's date and now.
            // This uses the function from your TripViewModel.
            val tripsAfterLastLogFlow = tripViewModel.getTripsBetweenDates(
                startDate = lastLogDate,
                endDate = Date(System.currentTimeMillis()) // Use current time as the end date
            )

            // Now, map the list of trips to the final calculated kilometrage.
            tripsAfterLastLogFlow.map { trips ->
                // Calculate the sum of distances from all subsequent trips.
                val subsequentTripsDistance = trips.sumOf { it.distance }
                val finalKilometrage = baseKilometrage + subsequentTripsDistance

                Log.d("DashboardVM", "Added $subsequentTripsDistance km from ${trips.size} trips. Final KM: $finalKilometrage")

                // Return the final calculated value.
                finalKilometrage
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = 0 // Initial value before any calculation is done
        )
        // The 'as StateFlow<Int>' cast is redundant and can be removed.
        // .stateIn() already returns a StateFlow.
    }

}