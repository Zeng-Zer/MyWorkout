package com.zeng.myworkout.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.zeng.myworkout.model.Exercise
import com.zeng.myworkout.model.WorkoutExerciseSql

@Dao
abstract class WorkoutExerciseDao : BaseDao<WorkoutExerciseSql>() {
    @Transaction
    @Query("SELECT * FROM exercise")
    abstract fun getAll(): LiveData<List<Exercise>>

    @Insert
    abstract suspend fun insertExercise(exercise: Exercise): Long

}