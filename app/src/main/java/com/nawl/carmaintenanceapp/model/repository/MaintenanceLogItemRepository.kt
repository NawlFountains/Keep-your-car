package com.nawl.carmaintenanceapp.model.repository

import com.nawl.carmaintenanceapp.model.dao.MaintenanceLogItemDao
import com.nawl.carmaintenanceapp.model.entities.MaintenanceLogItem

class MaintenanceLogItemRepository(
    private val dao: MaintenanceLogItemDao
): BaseRepository<MaintenanceLogItem>(dao) {
    suspend fun deleteByMaintenanceLogId(maintenanceLogId: Int) =
        dao.deleteByMaintenanceLogId(maintenanceLogId)

    suspend fun deleteByItemId(itemId: Int) =
        dao.deleteByItemId(itemId)
    }