package com.zeng.myworkout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeng.myworkout.model.Routine
import com.zeng.myworkout.repository.RoutineRepository
import kotlinx.coroutines.launch

class RoutineViewModel(private val repository: RoutineRepository) : ViewModel() {

    val routines: LiveData<List<Routine>> = repository.getAllRoutine()

    suspend fun insertRoutineSqlAsync(routine: Routine) : Long {
        return repository.insertRoutine(routine)
    }

    fun insertRoutineSql(routine: Routine) {
        viewModelScope.launch {
            repository.insertRoutine(routine)
        }
    }

    fun deleteRoutineSql(routine: Routine) {
        viewModelScope.launch {
            repository.deleteRoutine(routine)
        }
    }

    fun updateRoutineSql(routines: List<Routine>) {
        viewModelScope.launch {
            repository.update(routines)
        }
    }

    fun upsertRoutineSql(routines: List<Routine>) {
        viewModelScope.launch {
            repository.upsert(routines)
        }
    }
}