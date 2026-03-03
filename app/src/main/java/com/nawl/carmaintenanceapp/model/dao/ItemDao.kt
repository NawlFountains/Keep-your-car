package com.nawl.carmaintenanceapp.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nawl.carmaintenanceapp.model.entities.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("SELECT * FROM item")
    fun getAll(): Flow<List<Item>>

    @Insert
    suspend fun insert(item: Item)

    @Insert
    suspend fun insertAll(vararg items: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)
}