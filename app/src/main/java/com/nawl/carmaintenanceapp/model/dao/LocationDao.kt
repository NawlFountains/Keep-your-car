package com.nawl.carmaintenanceapp.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nawl.carmaintenanceapp.model.entities.Location
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * FROM location ORDER BY name DESC")
    fun getAll(): Flow<List<Location>>

    @Query("SELECT * FROM location WHERE id = :id")
    suspend fun getById(id: Int): Location?

    @Insert
    suspend fun insert(location: Location)

    @Update
    suspend fun update(location: Location)

    @Insert
    suspend fun insertAll(vararg locations: Location)

    @Delete
    suspend fun delete(location: Location)
}
