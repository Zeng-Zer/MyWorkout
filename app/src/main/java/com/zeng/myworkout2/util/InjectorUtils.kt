package com.zeng.myworkout2.util

import android.content.Context
import com.zeng.myworkout2.database.AppDatabase
import com.zeng.myworkout2.database.WorkoutRepository
import com.zeng.myworkout2.viewmodel.HomeViewModelFactory
import com.zeng.myworkout2.viewmodel.WorkoutViewModelFactory

object InjectorUtils {

    private fun getWorkoutRepository(context: Context): WorkoutRepository {
        val database = AppDatabase.getInstance(context.applicationContext)
        return WorkoutRepository.getInstance(
            database.workoutDao(),
            database.userDao()
        )
    }

    fun provideWorkoutViewModelFactory(context: Context): WorkoutViewModelFactory {
        val repository = getWorkoutRepository(context)
        return WorkoutViewModelFactory(repository)
    }

    fun provideHomeViewModelFactory(context: Context): HomeViewModelFactory {
        val repository = getWorkoutRepository(context)
        return HomeViewModelFactory(repository)
    }
}