package com.nawl.carmaintenanceapp.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nawl.carmaintenanceapp.model.entities.FuelLog

@Dao
interface FuelLogDao {
    @Query("SELECT * FROM fuel_logs")
    fun getAll(): List<FuelLog>

    @Insert
    fun insert(fuelLog: FuelLog)

    @Insert
    fun insertAll(vararg fuelLogs: FuelLog)

    @Delete
    fun delete(fuelLog: FuelLog)
}