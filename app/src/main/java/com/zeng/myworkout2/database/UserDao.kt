package com.zeng.myworkout2.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.zeng.myworkout2.model.User
import com.zeng.myworkout2.model.UserSql

@Dao
abstract class UserDao : BaseDao<UserSql>() {
    @Query("SELECT * FROM user")
    abstract fun getAll(): LiveData<List<User>>

    /**
     * TODO probable need to change this ?
     * id 0 = user
     */
    @Transaction
    @Query("SELECT * FROM user where id = 0")
    abstract fun getCurrentUser() : LiveData<User>

    @Transaction
    open suspend fun insertUser(user: User): Long {
        user.workoutId = user.workout?.id
        return insert(user)
    }
}
