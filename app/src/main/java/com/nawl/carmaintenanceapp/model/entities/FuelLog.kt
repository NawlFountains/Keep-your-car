package com.nawl.carmaintenanceapp.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.sql.Date

@Entity (
    tableName = "fuel_logs",
    foreignKeys = [
        ForeignKey(
            entity = Vehicle::class,
            parentColumns = ["vehicle_id"],
            childColumns = ["vehicle_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Location::class,
            parentColumns = ["location_id"],
            childColumns = ["station_location_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["vehicle_id"]),
        Index(value = ["station_location_id"])
    ]
)
data class FuelLog (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "vehicle_id") val vehicleId: Int,
    @ColumnInfo(name = "station_location_id") val stationId: String,
    @ColumnInfo(name = "fuel_litres") val quantity: Float,
    @ColumnInfo(name = "is_tank_full") val isTankFull: Boolean,
    @ColumnInfo(name = "kilometrage") val kilometrage: Int,
    @ColumnInfo(name = "date") val date: Date,
    @ColumnInfo(name = "notes") val notes: String
)