package com.zeng.myworkout2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeng.myworkout2.database.RoutineRepository
import com.zeng.myworkout2.model.Routine
import kotlinx.coroutines.launch

class RoutineViewModel(private val repository: RoutineRepository) : ViewModel() {

    val routines: LiveData<List<Routine>> = repository.getRoutines()

    fun addRoutine()  {
        val routine = Routine(emptyList(), "test", "test")
        viewModelScope.launch {
            repository.addRoutine(routine)
        }
    }
}