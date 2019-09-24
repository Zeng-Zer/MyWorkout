package com.zeng.myworkout2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeng.myworkout2.database.RoutineRepository
import com.zeng.myworkout2.model.Routine
import com.zeng.myworkout2.model.RoutineSql
import kotlinx.coroutines.launch

class RoutineViewModel(private val repository: RoutineRepository) : ViewModel() {

    val routines: LiveData<List<Routine>> = repository.getRoutines()

    suspend fun insertRoutineSql(routine: Routine) : Long {
        return repository.insert(routine)
    }

    fun deleteRoutineSql(routine: RoutineSql) {
        viewModelScope.launch {
            repository.delete(routine)
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