package com.zeng.myworkout2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeng.myworkout2.database.RoutineRepository
import com.zeng.myworkout2.database.WorkoutRepository
import com.zeng.myworkout2.model.RoutineSql
import com.zeng.myworkout2.model.WorkoutSql
import kotlinx.coroutines.launch

class RoutineDetailViewModel(
    private val routineRepo: RoutineRepository,
    private val workoutRepo: WorkoutRepository,
    routineId: Long
) : ViewModel() {

    val routine: LiveData<RoutineSql> = routineRepo.getRoutineSqlById(routineId)
    val workouts: LiveData<List<WorkoutSql>> = workoutRepo.getAllWorkoutSqlByRoutineId(routineId)

    fun addWorkoutSql(workout: WorkoutSql) {
        viewModelScope.launch {
            workoutRepo.insertWorkoutSql(workout)
        }
    }

}