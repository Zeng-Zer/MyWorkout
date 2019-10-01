package com.zeng.myworkout.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.zeng.myworkout.model.Workout
import com.zeng.myworkout.model.WorkoutExercise
import com.zeng.myworkout.model.WorkoutSql

@Dao
abstract class WorkoutDao : BaseDao<WorkoutSql>() {

    @Transaction
    @Query("SELECT * FROM workout WHERE id = :id")
    abstract fun getWorkoutById(id: Long): LiveData<Workout>

    @Transaction
    @Query("SELECT * FROM workout WHERE id = :id")
    abstract fun getWorkoutSqlById(id: Long): LiveData<WorkoutSql>

    @Transaction
    @Query("SELECT * FROM workout_exercise WHERE workoutId = :workoutId ORDER BY [order] ASC")
    abstract fun getAllWorkoutExerciseById(workoutId: Long): LiveData<List<WorkoutExercise>>

    @Transaction
    @Query("SELECT * FROM workout WHERE routineId = :routineId ORDER BY [order] ASC")
    abstract fun getAllWorkoutSqlByRoutineId(routineId: Long): LiveData<List<WorkoutSql>>

    @Transaction
    open suspend fun insertWorkout(workoutExerciseDao: WorkoutExerciseDao, workout: Workout): Long {
        val workoutId = insert(workout)
        workout.exercises.forEach {
            it.workoutId = workoutId
        }
        workoutExerciseDao.insertAllWorkoutExercise(workout.exercises)
        return workoutId
    }

}