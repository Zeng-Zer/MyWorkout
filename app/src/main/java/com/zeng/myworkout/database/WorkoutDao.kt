package com.zeng.myworkout.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.zeng.myworkout.model.Workout
import com.zeng.myworkout.model.WorkoutExerciseDetail

@Dao
abstract class WorkoutDao : BaseDao<Workout>() {

    @Transaction
    @Query("SELECT * FROM workout WHERE id = :id")
    abstract fun getWorkoutById(id: Long): LiveData<Workout?>

    @Transaction
    @Query("SELECT * FROM workout WHERE id = :id")
    abstract suspend fun workoutById(id: Long): Workout

    @Transaction
    @Query("SELECT * FROM workout WHERE reference = 0 AND finishDate IS NOT NULL ORDER BY [finishDate] DESC")
    abstract fun getAllFinishedWorkout(): LiveData<List<Workout>>

    @Transaction
    @Query("SELECT * FROM workout_exercise WHERE workoutId = :workoutId ORDER BY [order] ASC")
    abstract fun getAllWorkoutExerciseById(workoutId: Long): LiveData<List<WorkoutExerciseDetail>>

    @Transaction
    @Query("SELECT * FROM workout_exercise WHERE workoutId = :workoutId ORDER BY [order] ASC")
    abstract fun allWorkoutExerciseById(workoutId: Long): List<WorkoutExerciseDetail>

    @Transaction
    @Query("SELECT * FROM workout WHERE routineId = :routineId AND reference = 1 ORDER BY [order] ASC")
    abstract fun getAllReferenceWorkoutByRoutineId(routineId: Long): LiveData<List<Workout>>

    @Transaction
    @Query("SELECT * FROM workout WHERE routineId = :routineId AND reference = 1 ORDER BY [order] ASC")
    abstract suspend fun allReferenceWorkoutByRoutineId(routineId: Long): List<Workout>

    @Transaction
    @Query("DELETE FROM workout WHERE id = :workoutId")
    abstract suspend fun deleteWorkoutById(workoutId: Long)

    // TODO Probably need to optimize this
    @Transaction
    open suspend fun deleteWorkoutReorder(workout: Workout) {
        val workouts = allReferenceWorkoutByRoutineId(workout.routineId!!)
        val updateList = workouts
            .filter { w -> w.order > workout.order }
            .map { w -> w.order -= 1; w }

        delete(workout)
        update(updateList)
    }
}
