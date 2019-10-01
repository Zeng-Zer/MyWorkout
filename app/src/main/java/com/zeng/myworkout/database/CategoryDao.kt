package com.zeng.myworkout.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.zeng.myworkout.model.Category

@Dao
abstract class CategoryDao : BaseDao<Category>() {

    @Transaction
    @Query("SELECT * FROM category ORDER BY [id] ASC")
    abstract fun getAllCategory(): LiveData<List<Category>>
}
