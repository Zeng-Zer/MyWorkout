package com.zeng.myworkout2.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zeng.myworkout2.model.Exercise
import com.zeng.myworkout2.model.WorkoutExercise
import com.zeng.myworkout2.model.WorkoutExerciseSql

@Dao
abstract class ExerciseDao {
    @Transaction
    @Query("SELECT * FROM exercise")
    abstract fun getAll(): LiveData<List<Exercise>>

    @Insert
    abstract suspend fun insert(exercise: Exercise): Long

    @Insert
    abstract suspend fun insertAllWorkoutExerciseSql(exercises: List<WorkoutExerciseSql>)

    @Insert
    open suspend fun insertAllWorkoutExercise(workoutExercises: List<WorkoutExercise>) {
        val exercises = workoutExercises.map {
            val exerciseId = insert(it.exercise)
            it.exerciseId = exerciseId
            it
        }
        insertAllWorkoutExerciseSql(exercises)
    }

    @Delete
    abstract suspend fun delete(exercise: Exercise)
}