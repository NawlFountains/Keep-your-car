package com.nawl.carmaintenanceapp.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity (tableName = "trip_logs")
data class TripLog (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "origin") val origin: String,
    @ColumnInfo(name = "destination") val destination: String,
    @ColumnInfo(name = "distance") val distance: Int,
    @ColumnInfo(name = "unit") val unit: String,
    @ColumnInfo(name = "date") val date: Date
)