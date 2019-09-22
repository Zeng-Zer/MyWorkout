package com.zeng.myworkout2.util

import android.content.Context
import com.zeng.myworkout2.database.AppDatabase
import com.zeng.myworkout2.database.RoutineRepository
import com.zeng.myworkout2.database.WorkoutRepository

object RepositoryUtils {

    fun getWorkoutRepository(context: Context): WorkoutRepository {
        val database = AppDatabase.getInstance(context.applicationContext)
        return WorkoutRepository.getInstance(
            database.workoutDao(),
            database.exerciseDao(),
            database.userDao()
        )
    }

    fun getRoutineRepository(context: Context): RoutineRepository {
        val database = AppDatabase.getInstance(context.applicationContext)
        return RoutineRepository.getInstance(database.routineDao())
    }
}