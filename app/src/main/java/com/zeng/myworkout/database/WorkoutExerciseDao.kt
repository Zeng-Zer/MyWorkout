package com.zeng.myworkout.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.zeng.myworkout.model.Exercise
import com.zeng.myworkout.model.WorkoutExercise

@Dao
abstract class WorkoutExerciseDao : BaseDao<WorkoutExercise>() {
    @Transaction
    @Query("SELECT * FROM detail")
    abstract fun getAll(): LiveData<List<Exercise>>

    @Insert
    abstract suspend fun insertExercise(exercise: Exercise): Long

}