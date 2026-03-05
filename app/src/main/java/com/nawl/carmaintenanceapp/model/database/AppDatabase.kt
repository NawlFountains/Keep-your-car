package com.nawl.carmaintenanceapp.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nawl.carmaintenanceapp.model.dao.FuelLogDao
import com.nawl.carmaintenanceapp.model.dao.ItemDao
import com.nawl.carmaintenanceapp.model.dao.LocationDao
import com.nawl.carmaintenanceapp.model.dao.MaintenanceLogDao
import com.nawl.carmaintenanceapp.model.dao.MaintenanceLogItemDao
import com.nawl.carmaintenanceapp.model.dao.TripLogDao
import com.nawl.carmaintenanceapp.model.dao.VehicleDao
import com.nawl.carmaintenanceapp.model.entities.FuelLog
import com.nawl.carmaintenanceapp.model.entities.Item
import com.nawl.carmaintenanceapp.model.entities.Location
import com.nawl.carmaintenanceapp.model.entities.MaintenanceLog
import com.nawl.carmaintenanceapp.model.entities.MaintenanceLogItem
import com.nawl.carmaintenanceapp.model.entities.TripLog
import com.nawl.carmaintenanceapp.model.entities.Vehicle

@Database(
    entities = [
        MaintenanceLog::class,
        MaintenanceLogItem::class,
        FuelLog::class,
        TripLog::class,
        Vehicle::class,
        Location::class,
        Item::class
    ],
    version = 7
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun maintenanceLogDao(): MaintenanceLogDao
    abstract fun maintenanceLogItemDao(): MaintenanceLogItemDao
    abstract fun fuelLogDao(): FuelLogDao
    abstract fun tripLogDao(): TripLogDao
    abstract fun vehicleDao(): VehicleDao
    abstract fun locationDao(): LocationDao
    abstract fun itemDao(): ItemDao

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