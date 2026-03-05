package com.nawl.carmaintenanceapp

import android.app.Application
import com.nawl.carmaintenanceapp.model.database.AppDatabase
import com.nawl.carmaintenanceapp.model.entities.FuelLog
import com.nawl.carmaintenanceapp.model.repository.BaseRepository


class MainApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    val maintenanceLogRepository by lazy { BaseRepository( database.fuelLogDao()) }
    val fuelLogRepository by lazy { BaseRepository( database.fuelLogDao()) }
    val tripLogRepository by lazy { BaseRepository( database.tripLogDao()) }
    val vehicleRepository by lazy { BaseRepository( database.vehicleDao()) }
    val locationRepository by lazy { BaseRepository( database.locationDao()) }
    val itemRepository by lazy { BaseRepository( database.itemDao()) }
    val maintenanceLogItemRepository by lazy { BaseRepository( database.maintenanceLogItemDao()) }

}