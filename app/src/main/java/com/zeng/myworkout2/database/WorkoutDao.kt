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
    abstract fun getWorkoutById(id: Long): LiveData<Workout>

    @Transaction
    @Query("SELECT * FROM workout WHERE id =:id")
    abstract fun getWorkoutSqlById(id: Long): LiveData<WorkoutSql>

    @Transaction
    @Query("SELECT * FROM workout_exercise WHERE workoutId = :workoutId ORDER BY [order] ASC")
    abstract suspend fun getExerciseDetailList(workoutId: Long): List<WorkoutExerciseSql>

    @Delete
    abstract suspend fun delete(workoutSql: WorkoutSql)

    @Insert
    abstract suspend fun insertWorkoutSql(workoutSql: WorkoutSql): Long

    @Transaction
    open suspend fun insert(exerciseDao: ExerciseDao, workout: Workout): Long {
        val workoutId = insertWorkoutSql(workout)
        workout.exercises.forEach {
            it.workoutId = workoutId
        }
        exerciseDao.insertAllWorkoutExercise(workout.exercises)
        return workoutId
    }

}
