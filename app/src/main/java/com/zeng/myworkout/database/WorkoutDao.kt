package com.zeng.myworkout.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.zeng.myworkout.model.Workout
import com.zeng.myworkout.model.WorkoutExerciseDetail
import com.zeng.myworkout.model.WorkoutName

@Dao
abstract class WorkoutDao : BaseDao<Workout>() {

    @Transaction
    @Query("SELECT * FROM workout WHERE id = :id")
    abstract fun getWorkoutById(id: Long): LiveData<Workout?>

    @Transaction
    @Query("SELECT * FROM workout_exercise WHERE workoutId = :workoutId ORDER BY [order] ASC")
    abstract fun getAllWorkoutExerciseById(workoutId: Long): LiveData<List<WorkoutExerciseDetail>>

    @Transaction
    @Query("SELECT * FROM workout_exercise WHERE workoutId = :workoutId ORDER BY [order] ASC")
    abstract suspend fun allWorkoutExerciseById(workoutId: Long): List<WorkoutExerciseDetail>

    @Transaction
    @Query("SELECT id, name FROM workout WHERE routineId = :routineId AND reference = 1 ORDER BY [order] ASC")
    abstract fun getAllReferenceWorkoutNamesByRoutineId(routineId: Long): LiveData<List<WorkoutName>>

    @Transaction
    @Query("SELECT * FROM workout WHERE id = :id")
    abstract fun workoutById(id: Long): Workout

    @Transaction
    @Query("SELECT * FROM workout WHERE routineId = :routineId AND reference = 1 ORDER BY [order] ASC")
    abstract suspend fun allReferenceWorkoutByRoutineId(routineId: Long): List<Workout>

    @Transaction
    @Query("DELETE FROM workout WHERE id = :workoutId")
    abstract suspend fun deleteWorkoutById(workoutId: Long)

    // TODO Probably need to optimize this
    @Transaction
    open suspend fun deleteWorkoutReorderById(workoutId: Long) {
        val workout = workoutById(workoutId)
        val workouts = allReferenceWorkoutByRoutineId(workout.routineId!!)
        val updateList = workouts
            .filter { w -> w.order > workout.order }
            .map { w -> w.order -= 1; w }

        delete(workout)
        update(updateList)
    }
}
