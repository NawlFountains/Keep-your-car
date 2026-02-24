package com.nawl.carmaintenanceapp.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nawl.carmaintenanceapp.model.entities.TripLog

@Dao
interface TripLogDao {
    @Query("SELECT * FROM trip_logs")
    fun getAll(): List<TripLog>

    @Insert
    fun insert(tripLog: TripLog)

    @Insert
    fun insertAll(vararg tripLogs: TripLog)

    @Delete
    fun delete(tripLog: TripLog)
}
