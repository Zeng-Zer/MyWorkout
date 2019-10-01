package com.zeng.myworkout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeng.myworkout.model.Routine
import com.zeng.myworkout.repository.RoutineRepository
import kotlinx.coroutines.launch

class RoutineViewModel(private val repository: RoutineRepository) : ViewModel() {

    val routines: LiveData<List<Routine>> = repository.getAllRoutine()

    suspend fun insertRoutineAsync(routine: Routine) : Long {
        return repository.insertRoutine(routine)
    }

    fun deleteRoutine(routine: Routine) {
        viewModelScope.launch {
            repository.deleteRoutine(routine)
        }
    }

    fun updateRoutine(routines: List<Routine>) {
        viewModelScope.launch {
            repository.update(routines)
        }
    }

    fun upsertRoutine(routines: List<Routine>) {
        viewModelScope.launch {
            repository.upsert(routines)
        }
    }
}