package com.nawl.carmaintenanceapp.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nawl.carmaintenanceapp.model.entities.MaintenanceLog
import kotlinx.coroutines.flow.Flow

@Dao
interface MaintenanceLogDao {
    @Query("SELECT * FROM maintenance_log ORDER BY date DESC")
    fun getAll(): Flow<List<MaintenanceLog>>

    @Query("SELECT * FROM maintenance_log ORDER BY date DESC LIMIT :amount")
    fun getLatestLogs(amount: Int): Flow<List<MaintenanceLog>>

    @Query("SELECT * FROM maintenance_log WHERE date = :date")
    fun getByDate(date: Long): List<MaintenanceLog>

    @Insert
    suspend fun insert(maintenanceLog: MaintenanceLog)

    @Insert
    fun insertAll(vararg maintenanceLogs: MaintenanceLog)

    @Update
    suspend fun update (maintenanceLog: MaintenanceLog)


    @Delete
    suspend fun delete (maintenanceLog: MaintenanceLog)
}