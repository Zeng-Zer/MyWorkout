package com.zeng.myworkout2.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zeng.myworkout2.model.User
import com.zeng.myworkout2.model.UserSql
import com.zeng.myworkout2.model.WorkoutSql

@Dao
interface UserDao {
    @Transaction
    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<UserSql>>

    /**
     * id 0 equals phone it
     */
    @Transaction
    @Query("SELECT * FROM user where id = 0")
    fun getCurrentUser() : LiveData<User>

    @Insert
    suspend fun insert(userSql: UserSql): Long

    @Insert
    suspend fun insertAll(vararg workoutSqls: WorkoutSql): List<Long>

    @Update
    suspend fun update(userSql: UserSql)

    @Delete
    suspend fun delete(workoutSql: WorkoutSql)
}
