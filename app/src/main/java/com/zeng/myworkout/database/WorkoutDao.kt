package com.zeng.myworkout.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.zeng.myworkout.model.Workout
import com.zeng.myworkout.model.WorkoutExercise

@Dao
abstract class WorkoutDao : BaseDao<Workout>() {

    @Transaction
    @Query("SELECT * FROM workout WHERE id = :id")
    abstract fun getWorkoutById(id: Long): LiveData<Workout>

    @Transaction
    @Query("SELECT * FROM workout_exercise WHERE workoutId = :workoutId ORDER BY [order] ASC")
    abstract fun getAllWorkoutExerciseById(workoutId: Long): LiveData<List<WorkoutExercise>>

    @Transaction
    @Query("SELECT * FROM workout WHERE routineId = :routineId ORDER BY [order] ASC")
    abstract fun getAllWorkoutByRoutineId(routineId: Long): LiveData<List<Workout>>

}
