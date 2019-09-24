package com.zeng.myworkout2.database

import androidx.room.*

@Dao
abstract class BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(obj: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(objs: List<T>): List<Long>

    @Update
    abstract suspend fun update(obj: T)

    @Update
    abstract suspend fun update(objs: List<T>)

    @Delete
    abstract suspend fun delete(obj: T)

    @Transaction
    open suspend fun upsert(obj: T) {
        if (insert(obj) == -1L) {
            update(obj)
        }
    }

    @Transaction
    open suspend fun upsert(objs: List<T>) {
        val resultIds = insert(objs)
        val updateList = (resultIds zip objs)
            .filter { (id, _) -> id == -1L }
            .map { (_, obj) -> obj }

        update(updateList)
    }
}