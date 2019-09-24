package com.zeng.myworkout2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zeng.myworkout2.database.RoutineRepository
import com.zeng.myworkout2.database.WorkoutRepository
import com.zeng.myworkout2.model.RoutineSql
import com.zeng.myworkout2.model.WorkoutSql

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