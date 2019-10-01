package com.zeng.myworkout.util

import android.content.Context
import com.zeng.myworkout.database.AppDatabase
import com.zeng.myworkout.database.ExerciseRepository
import com.zeng.myworkout.database.RoutineRepository
import com.zeng.myworkout.database.WorkoutRepository

object RepositoryUtils {

    fun getWorkoutRepository(context: Context): WorkoutRepository {
        val database = AppDatabase.getInstance(context.applicationContext)
        return WorkoutRepository.getInstance(
            database.workoutDao(),
            database.workoutExerciseDao(),
            database.userDao()
        )
    }

    fun getRoutineRepository(context: Context): RoutineRepository {
        val database = AppDatabase.getInstance(context.applicationContext)
        return RoutineRepository.getInstance(database.routineDao())
    }

    fun getExerciseRepository(context: Context): ExerciseRepository {
        val database = AppDatabase.getInstance(context.applicationContext)
        return ExerciseRepository.getInstance(database.exerciseDao(), database.categoryDao())
    }
}