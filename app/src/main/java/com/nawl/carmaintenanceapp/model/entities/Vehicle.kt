package com.nawl.carmaintenanceapp.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "vehicle")
data class Vehicle(
    @PrimaryKey(autoGenerate = true) val vehicleId: Long = 0,
    @ColumnInfo(name = "name") val name: String
)