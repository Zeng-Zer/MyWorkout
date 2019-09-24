package com.zeng.myworkout2.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.zeng.myworkout2.model.Exercise
import com.zeng.myworkout2.model.WorkoutExercise
import com.zeng.myworkout2.model.WorkoutExerciseSql

@Dao
abstract class WorkoutExerciseDao : BaseDao<WorkoutExerciseSql>() {
    @Transaction
    @Query("SELECT * FROM exercise")
    abstract fun getAll(): LiveData<List<Exercise>>

    @Insert
    abstract suspend fun insertExercise(exercise: Exercise): Long

    @Insert
    open suspend fun insertAllWorkoutExercise(workoutExercises: List<WorkoutExercise>) {
        workoutExercises.forEach {
            it.exerciseId = it.exercise.id
        }
        insert(workoutExercises)
    }
}