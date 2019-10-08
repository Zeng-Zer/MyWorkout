package com.zeng.myworkout.di

import com.zeng.myworkout.database.AppDatabase
import com.zeng.myworkout.repository.ExerciseRepository
import com.zeng.myworkout.repository.RoutineRepository
import com.zeng.myworkout.repository.WorkoutRepository
import com.zeng.myworkout.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Room Database
    single { AppDatabase.buildDatabase(get()) }

    // DAO
    single { get<AppDatabase>().loadDao() }
    single { get<AppDatabase>().exerciseDao() }
    single { get<AppDatabase>().categoryDao() }
    single { get<AppDatabase>().routineDao() }
    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().workoutDao() }
    single { get<AppDatabase>().workoutExerciseDao() }

    // Repositories
    single { ExerciseRepository(get(), get()) }
    single { WorkoutRepository(get(), get(), get(), get()) }
    single { RoutineRepository(get()) }

    viewModel { HomeViewModel(get(), get()) }
}
