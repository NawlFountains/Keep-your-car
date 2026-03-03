package com.nawl.carmaintenanceapp.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nawl.carmaintenanceapp.model.entities.TripLog
import kotlinx.coroutines.flow.Flow
import java.sql.Date

@Dao
interface TripLogDao {
    @Query("SELECT * FROM trip_logs ORDER BY date DESC")
    fun getAll(): Flow<List<TripLog>>

    @Query("SELECT * FROM trip_logs ORDER BY date DESC LIMIT :amount")
    fun getLatestLogs(amount: Int): Flow<List<TripLog>>

    @Query("SELECT * FROM trip_logs WHERE date BETWEEN :startDate AND :endDate")
    fun getTripsBetween(startDate: Date, endDate: Date): Flow<List<TripLog>>

    @Insert
    suspend fun insert(tripLog: TripLog)

    @Update
    suspend fun update(tripLog: TripLog)


    @Insert
    suspend fun insertAll(vararg tripLogs: TripLog)

    @Delete
    suspend fun delete(tripLog: TripLog)
}
