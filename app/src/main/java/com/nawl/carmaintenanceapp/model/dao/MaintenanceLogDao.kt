package com.nawl.carmaintenanceapp.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nawl.carmaintenanceapp.model.entities.MaintenanceLog

@Dao
interface MaintenanceLogDao {
    @Query("SELECT * FROM maintenance_log")
    fun getAll(): List<MaintenanceLog>

    @Insert
    fun insert(maintenanceLog: MaintenanceLog)

    @Insert
    fun insertAll(vararg maintenanceLogs: MaintenanceLog)

    @Delete
    fun delete (maintenanceLog: MaintenanceLog)
}