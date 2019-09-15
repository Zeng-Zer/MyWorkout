package com.zeng.myworkout2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zeng.myworkout2.database.WorkoutRepository

class WorkoutViewModelFactory(private val repository: WorkoutRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WorkoutViewModel(repository) as T
    }
}