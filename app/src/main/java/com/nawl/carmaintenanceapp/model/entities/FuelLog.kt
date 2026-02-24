package com.nawl.carmaintenanceapp.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "fuel_logs")
data class FuelLog (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "station_name") val stationName: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "quantity") val quantity: String,
    @ColumnInfo(name = "is_tank_full") val isTankFull: Boolean,
    @ColumnInfo(name = "mileage") val mileage: String,
    @ColumnInfo(name = "notes") val notes: String
)