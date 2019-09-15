package com.zeng.myworkout2.util

import android.content.Context
import com.zeng.myworkout2.database.AppDatabase
import com.zeng.myworkout2.database.RoutineRepository
import com.zeng.myworkout2.database.WorkoutRepository
import com.zeng.myworkout2.viewmodel.HomeViewModelFactory
import com.zeng.myworkout2.viewmodel.RoutineViewModelFactory

object InjectorUtils {

    private fun getWorkoutRepository(context: Context): WorkoutRepository {
        val database = AppDatabase.getInstance(context.applicationContext)
        return WorkoutRepository.getInstance(
            database.workoutDao(),
            database.userDao()
        )
    }

    private fun getRoutineRepository(context: Context): RoutineRepository {
        val database = AppDatabase.getInstance(context.applicationContext)
        return RoutineRepository.getInstance(database.routineDao())
    }

    fun provideHomeViewModelFactory(context: Context): HomeViewModelFactory {
        val repository = getWorkoutRepository(context)
        return HomeViewModelFactory(repository)
    }

    fun provideRoutineViewModelFactory(context: Context): RoutineViewModelFactory {
        val repository = getRoutineRepository(context)
        return RoutineViewModelFactory(repository)
    }
}