package com.nawl.carmaintenanceapp.model.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import com.nawl.carmaintenanceapp.model.entities.Item
import com.nawl.carmaintenanceapp.model.entities.MaintenanceLog
import com.nawl.carmaintenanceapp.model.entities.MaintenanceLogItem
import kotlinx.coroutines.flow.Flow

data class MaintenanceLogWithItems(
    @Embedded val maintenanceLog: MaintenanceLog,
    @Relation(
        parentColumn = "maintenance_log_id",
        entityColumn = "item_id",
        associateBy = Junction(MaintenanceLogItem::class)
    )
    val items: List<Item>
)