package com.nawl.carmaintenanceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nawl.carmaintenanceapp.MainApplication

// This MainViewModel now holds all the other ViewModels.
class MainViewModel(application: MainApplication) : ViewModel() {

    // Create instances of all the other ViewModels right here.
    val maintenanceViewModel: MaintenanceViewModel = MaintenanceViewModel(application.database.maintenanceLogDao())
    val tripViewModel: TripViewModel = TripViewModel(application.database.tripLogDao())
    val fuelViewModel: FuelViewModel = FuelViewModel(application.database.fuelLogDao())

    // The DashboardViewModel is also created here, using the others as dependencies.
    val homeViewModel: HomeViewModel = HomeViewModel(
        maintenanceViewModel = maintenanceViewModel,
        tripViewModel = tripViewModel,
        fuelViewModel = fuelViewModel
    )
}

// A simple factory for our new MainViewModel
class MainViewModelFactory(private val application: MainApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}