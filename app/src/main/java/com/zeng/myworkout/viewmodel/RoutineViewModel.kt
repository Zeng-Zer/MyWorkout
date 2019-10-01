package com.zeng.myworkout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeng.myworkout.repository.RoutineRepository
import com.zeng.myworkout.model.Routine
import com.zeng.myworkout.model.RoutineSql
import kotlinx.coroutines.launch

class RoutineViewModel(private val repository: RoutineRepository) : ViewModel() {

    val routines: LiveData<List<Routine>> = repository.getRoutines()

    suspend fun insertRoutineSqlAsync(routine: RoutineSql) : Long {
        return repository.insertRoutine(routine)
    }

    fun insertRoutineSql(routine: RoutineSql) {
        viewModelScope.launch {
            repository.insertRoutine(routine)
        }
    }

    fun deleteRoutineSql(routine: RoutineSql) {
        viewModelScope.launch {
            repository.deleteRoutine(routine)
        }
    }

    fun updateRoutineSql(routines: List<RoutineSql>) {
        viewModelScope.launch {
            repository.update(routines)
        }
    }

    fun upsertRoutineSql(routines: List<RoutineSql>) {
        viewModelScope.launch {
            repository.upsert(routines)
        }
    }
}