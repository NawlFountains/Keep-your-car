package com.nawl.carmaintenanceapp.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nawl.carmaintenanceapp.model.entities.MaintenanceLogItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MaintenanceLogItemDao: BaseDao<MaintenanceLogItem> {
    @Query("SELECT * FROM maintenance_log_item")
    fun getAll(): Flow<List<MaintenanceLogItem>>

    @Query("SELECT * FROM maintenance_log_item WHERE maintenance_log_id = :maintenanceLogId")
    fun getItemsByMaintenanceLogId(maintenanceLogId: Int): Flow<List<MaintenanceLogItem>>
    @Query("DELETE FROM maintenance_log_item WHERE maintenance_log_id = :maintenanceLogId")
    suspend fun deleteByMaintenanceLogId(maintenanceLogId: Int)

    @Query("DELETE FROM maintenance_log_item WHERE item_id = :itemId")
    suspend fun deleteByItemId(itemId: Int)  // ← defined here

}