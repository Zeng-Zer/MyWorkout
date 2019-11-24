package com.zeng.myworkout.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.zeng.myworkout.model.Category

@Dao
abstract class CategoryDao : BaseDao<Category>() {

    @Transaction
    @Query("SELECT * FROM category ORDER BY [id] ASC")
    abstract suspend fun getAllCategory(): List<Category>
}
