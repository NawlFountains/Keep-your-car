package com.nawl.carmaintenanceapp

import android.app.Application
import com.nawl.carmaintenanceapp.model.AppDatabase

class MainApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}