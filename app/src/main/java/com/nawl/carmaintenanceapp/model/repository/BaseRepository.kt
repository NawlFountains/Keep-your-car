package com.nawl.carmaintenanceapp.model.repository

import com.nawl.carmaintenanceapp.model.dao.BaseDao

open class BaseRepository<E>(private val baseDao: BaseDao<E>) {
    suspend fun insert(entity: E): Long {
        return baseDao.insert(entity)
    }
    suspend fun insertAll(vararg entities: E) {
        baseDao.insertAll(*entities)
    }
    suspend fun update(entity: E) {
        baseDao.update(entity)
    }
    suspend fun delete(entity: E) {
        baseDao.delete(entity)
    }
}