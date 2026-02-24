package com.nawl.carmaintenanceapp.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "maintenance_log")
data class MaintenanceLog(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "item_changed") val itemChanged: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "mileage") val mileage: String,
    @ColumnInfo(name = "unit") val unit: String,
    @ColumnInfo(name = "notes") val notes: String
)