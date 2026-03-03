package com.nawl.carmaintenanceapp.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nawl.carmaintenanceapp.model.entities.MaintenanceLogItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MaintenanceLogItemDao {
    @Query("SELECT * FROM maintenance_log_item")
    fun getAll(): Flow<List<MaintenanceLogItem>>

    @Query("SELECT * FROM maintenance_log_item WHERE maintenance_log_id = :maintenanceLogId")
    fun getItemsByMaintenanceLogId(maintenanceLogId: Int): Flow<List<MaintenanceLogItem>>

    @Insert
    suspend fun insert(maintenanceLogItem: MaintenanceLogItem)

    @Insert
    fun insertAll(vararg maintenanceLogItems: MaintenanceLogItem)

    @Update
    suspend fun update (maintenanceLogItem: MaintenanceLogItem)


    @Delete
    suspend fun delete (maintenanceLogItem: MaintenanceLogItem)
}