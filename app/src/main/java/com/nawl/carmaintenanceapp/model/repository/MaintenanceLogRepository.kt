package com.nawl.carmaintenanceapp.model.repository

import com.nawl.carmaintenanceapp.model.dao.MaintenanceLogDao
import com.nawl.carmaintenanceapp.model.entities.Item
import com.nawl.carmaintenanceapp.model.entities.Location
import com.nawl.carmaintenanceapp.model.entities.MaintenanceLogItem
import com.nawl.carmaintenanceapp.model.entities.Vehicle
import com.nawl.carmaintenanceapp.model.repository.BaseRepository
import com.nawl.carmaintenanceapp.model.entities.MaintenanceLog


class MaintenanceLogRepository(
    private val maintenanceLogDao: MaintenanceLogDao,
    private val itemRepository: BaseRepository<Item>,
    private val locationRepository: BaseRepository<Location>,
    private val maintenanceLogItemRepository: BaseRepository<MaintenanceLogItem>

): BaseRepository<MaintenanceLog>(maintenanceLogDao) {

    suspend fun insertWithDetails(
        maintenanceLog: MaintenanceLog,
        items: List<Item>,
        location: Location
    ) {
        locationRepository.insert(location)

        val logId = maintenanceLogDao.insert(maintenanceLog)

        items.forEach { item ->
            val itemId = itemRepository.insert(item)
            maintenanceLogItemRepository.insert(MaintenanceLogItem(logId, itemId))
        }
    }
}