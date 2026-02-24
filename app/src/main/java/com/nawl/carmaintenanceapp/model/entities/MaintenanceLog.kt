package com.nawl.carmaintenanceapp.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity (tableName = "maintenance_log")
data class MaintenanceLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "item_changed") val itemChanged: String,
    @ColumnInfo(name = "date") val date: Date,
    @ColumnInfo(name = "mileage") val mileage: Int,
    @ColumnInfo(name = "unit") val unit: String,
    @ColumnInfo(name = "notes") val notes: String
)