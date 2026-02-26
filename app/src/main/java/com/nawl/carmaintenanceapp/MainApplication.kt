package com.nawl.carmaintenanceapp

import android.app.Application
import com.nawl.carmaintenanceapp.model.database.AppDatabase

class MainApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}