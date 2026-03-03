package com.nawl.carmaintenanceapp.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity (
    tableName = "maintenance_log_item",
    primaryKeys = ["maintenance_log_id", "item_id"],
    foreignKeys = [
        ForeignKey(
            entity = MaintenanceLog::class,
            parentColumns = ["maintenance_log_id"],
            childColumns = ["maintenance_log_id"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(
            entity = Item::class,
            parentColumns = ["item_id"],
            childColumns = ["item_id"],
        )
    ])

data class MaintenanceLogItem(
    @ColumnInfo(name = "maintenance_log_id") val maintenanceLogId: Int,
    @ColumnInfo(name = "item_id") val itemId: Int,
)