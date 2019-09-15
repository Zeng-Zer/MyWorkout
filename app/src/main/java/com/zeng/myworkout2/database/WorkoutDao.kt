package com.zeng.myworkout2.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zeng.myworkout2.model.Workout
import com.zeng.myworkout2.model.WorkoutExerciseSql
import com.zeng.myworkout2.model.WorkoutSql

@Dao
abstract class WorkoutDao {

    @Transaction
    @Query("SELECT * FROM workout WHERE id = :id")
    abstract fun getWorkoutById(id: Long): LiveData<WorkoutSql>

    @Transaction
    @Query("SELECT * FROM workout_exercise WHERE workoutId = :workoutId")
    abstract fun getExerciseDetailList(workoutId: Long): List<WorkoutExerciseSql>

    @Delete
    abstract fun delete(workoutSql: WorkoutSql)

    @Insert
    abstract fun insert(workoutSql: WorkoutSql): Long

    @Transaction
    open suspend fun insert(exerciseDao: ExerciseDao, workout: Workout): Long {
        val workoutId = insert(workout)
        workout.exercises.forEach {
            it.workoutId = workoutId
        }
        exerciseDao.insertWorkoutExercises(workout.exercises)
        return workoutId
    }

}
