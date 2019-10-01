package com.zeng.myworkout.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.zeng.myworkout.model.User

@Dao
abstract class UserDao : BaseDao<User>() {
    @Transaction
    @Query("SELECT * FROM user")
    abstract fun getAll(): LiveData<List<User>>

    /**
     * TODO probable need to change this ?
     * id 0 = user
     */
    @Transaction
    @Query("SELECT * FROM user where id = 0")
    abstract fun getCurrentUser() : LiveData<User>
}
