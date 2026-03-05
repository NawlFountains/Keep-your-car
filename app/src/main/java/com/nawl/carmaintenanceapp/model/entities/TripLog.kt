package com.nawl.carmaintenanceapp.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.sql.Date

@Entity (
    tableName = "trip_logs",
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
            childColumns = ["origin_location_id"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = Location::class,
            parentColumns = ["location_id"],
            childColumns = ["destination_location_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["vehicle_id"]),
        Index(value = ["origin_location_id"]),
        Index(value = ["destination_location_id"])
    ]
)
data class TripLog (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "vehicle_id") val vehicleId: Long,
    @ColumnInfo(name = "origin_location_id") val originLocationId: Long,
    @ColumnInfo(name = "destination_location_id") val destinationLocationId: Long,
    @ColumnInfo(name = "distance_km") val distanceKm: Int,
    @ColumnInfo(name = "date") val date: Date
)