package com.nawl.carmaintenanceapp.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.sql.Date

@Entity (tableName = "maintenance_log",
    foreignKeys = [
        ForeignKey(
            entity = Vehicle::class,
            parentColumns = ["vehicle_id"],
            childColumns = ["vehicle_id"],
        )
    ],
    indices = [
        Index(value = ["vehicle_id"])
    ]
)
data class MaintenanceLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "vehicle_id") val vehicleId: Int,
    @ColumnInfo(name = "date") val date: Date,
    @ColumnInfo(name = "kilometrage") val kilometrage: Int,
    @ColumnInfo(name = "notes") val notes: String
)