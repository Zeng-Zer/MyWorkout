package com.zeng.myworkout2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zeng.myworkout2.database.RoutineRepository
import com.zeng.myworkout2.model.Routine

class RoutineDetailViewModel(
    private val repository: RoutineRepository,
    routineId: Long
) : ViewModel() {

    val routine: LiveData<Routine> = repository.getRoutineById(routineId)

}