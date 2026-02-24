package com.nawl.carmaintenanceapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nawl.carmaintenanceapp.model.dao.FuelLogDao
import com.nawl.carmaintenanceapp.model.dao.MaintenanceLogDao
import com.nawl.carmaintenanceapp.model.dao.TripLogDao
import com.nawl.carmaintenanceapp.model.database.Converters
import com.nawl.carmaintenanceapp.model.entities.FuelLog
import com.nawl.carmaintenanceapp.model.entities.MaintenanceLog
import com.nawl.carmaintenanceapp.model.entities.TripLog

@Database(entities = [MaintenanceLog::class, FuelLog::class, TripLog::class], version = 3)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun maintenanceLogDao(): MaintenanceLogDao
    abstract fun fuelLogDao(): FuelLogDao
    abstract fun tripLogDao(): TripLogDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "car-maintenance-database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
