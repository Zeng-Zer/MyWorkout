package com.zeng.myworkout.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.zeng.myworkout.model.User

@Dao
abstract class UserDao : BaseDao<User>() {

    @Transaction
    @Query("SELECT * FROM user where current = 1")
    abstract fun getCurrentUser() : LiveData<User>
}
