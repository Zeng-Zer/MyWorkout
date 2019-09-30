package com.zeng.myworkout2.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.zeng.myworkout2.model.Exercise

@Dao
abstract class ExerciseDao : BaseDao<Exercise>() {

    @Transaction
    @Query("SELECT * FROM exercise WHERE id = :id")
    abstract fun getExerciseById(id: Long): LiveData<Exercise>

    @Transaction
    @Query("SELECT * FROM exercise ORDER BY [name] ASC")
    abstract fun getAllExercise(): LiveData<List<Exercise>>
}
