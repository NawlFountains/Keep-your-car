package com.nawl.carmaintenanceapp.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nawl.carmaintenanceapp.model.entities.FuelLog
import com.nawl.carmaintenanceapp.model.entities.TripLog
import kotlinx.coroutines.flow.Flow

@Dao
interface FuelLogDao: BaseDao<FuelLog> {
    @Query("SELECT * FROM fuel_logs ORDER BY date DESC")
    fun getAll(): Flow<List<FuelLog>>

    @Query("SELECT * FROM fuel_logs ORDER BY date DESC LIMIT :amount")
    fun getLatestLogs(amount: Int): Flow<List<FuelLog>>

}