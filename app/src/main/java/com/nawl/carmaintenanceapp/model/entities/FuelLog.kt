package com.nawl.carmaintenanceapp.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity (tableName = "fuel_logs")
data class FuelLog (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "station_name") val stationName: String,
    @ColumnInfo(name = "date") val date: Date,
    @ColumnInfo(name = "quantity") val quantity: Float,
    @ColumnInfo(name = "is_tank_full") val isTankFull: Boolean,
    @ColumnInfo(name = "kilometrage") val kilometrage: Int,
    @ColumnInfo(name = "notes") val notes: String
)