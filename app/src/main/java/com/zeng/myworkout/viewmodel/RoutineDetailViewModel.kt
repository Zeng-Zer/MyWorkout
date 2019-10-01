package com.zeng.myworkout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zeng.myworkout.repository.RoutineRepository
import com.zeng.myworkout.repository.WorkoutRepository
import com.zeng.myworkout.model.RoutineSql
import com.zeng.myworkout.model.WorkoutSql

class RoutineDetailViewModel(
    private val routineRepo: RoutineRepository,
    private val workoutRepo: WorkoutRepository,
    routineId: Long
) : ViewModel() {

    val routine: LiveData<RoutineSql> = routineRepo.getRoutineSqlById(routineId)
    val workouts: LiveData<List<WorkoutSql>> = workoutRepo.getAllWorkoutSqlByRoutineId(routineId)

    suspend fun addWorkoutSql(workout: WorkoutSql) {
        workoutRepo.insertWorkoutSql(workout)
    }

}