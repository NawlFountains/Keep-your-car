package com.nawl.carmaintenanceapp.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nawl.carmaintenanceapp.model.entities.FuelLog
import kotlinx.coroutines.flow.Flow

@Dao
interface FuelLogDao {
    @Query("SELECT * FROM fuel_logs")
    fun getAll(): Flow<List<FuelLog>>

    @Query("SELECT * FROM fuel_logs ORDER BY date DESC LIMIT :amount")
    fun getLatestLogs(amount: Int): Flow<List<FuelLog>>

    @Insert
    suspend fun insert(fuelLog: FuelLog)

    @Insert
    suspend fun insertAll(vararg fuelLogs: FuelLog)

    @Delete
    suspend fun delete(fuelLog: FuelLog)
}