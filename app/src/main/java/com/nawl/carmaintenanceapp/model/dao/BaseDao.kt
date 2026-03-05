package com.nawl.carmaintenanceapp.model.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface BaseDao<T> {
    @Insert
    suspend fun insert(entity: T): Long

    @Insert
    suspend fun insertAll(vararg entities: T)

    @Update
    suspend fun update(entity: T)

    @Delete
    suspend fun delete(entity: T)
}
