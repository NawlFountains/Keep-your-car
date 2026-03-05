package com.nawl.carmaintenanceapp.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nawl.carmaintenanceapp.model.entities.Vehicle
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao: BaseDao<Vehicle> {

    @Query("SELECT * FROM item")
    fun getAll(): Flow<List<Vehicle>>
}