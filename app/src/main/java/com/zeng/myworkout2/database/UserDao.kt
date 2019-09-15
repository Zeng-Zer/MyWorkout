package com.zeng.myworkout2.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zeng.myworkout2.model.User
import com.zeng.myworkout2.model.UserSql
import com.zeng.myworkout2.model.WorkoutSql

@Dao
abstract class UserDao {
    @Transaction
    @Query("SELECT * FROM user")
    abstract fun getAll(): LiveData<List<User>>

    /**
     * id 0 equals phone it
     */
    @Transaction
    @Query("SELECT * FROM user where id = 0")
    abstract fun getCurrentUser() : LiveData<User>

    @Insert
    abstract suspend fun insertUserSql(user: UserSql): Long

    @Transaction
    open suspend fun insert(user: User): Long {
        user.workoutId = user.workout?.id
        return insertUserSql(user)
    }

    @Update
    abstract suspend fun update(userSql: UserSql)

    @Delete
    abstract suspend fun delete(workoutSql: WorkoutSql)
}
