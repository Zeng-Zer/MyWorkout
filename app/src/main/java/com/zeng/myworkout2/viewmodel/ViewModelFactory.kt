package com.zeng.myworkout2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zeng.myworkout2.database.RoutineRepository
import com.zeng.myworkout2.database.WorkoutRepository

class HomeViewModelFactory(private val repository: WorkoutRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}

class RoutineViewModelFactory(private val repository: RoutineRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RoutineViewModel(repository) as T
    }
}
