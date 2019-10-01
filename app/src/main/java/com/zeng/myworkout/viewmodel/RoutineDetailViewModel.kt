package com.zeng.myworkout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zeng.myworkout.model.Routine
import com.zeng.myworkout.model.Workout
import com.zeng.myworkout.repository.RoutineRepository
import com.zeng.myworkout.repository.WorkoutRepository

class RoutineDetailViewModel(
    private val routineRepo: RoutineRepository,
    private val workoutRepo: WorkoutRepository,
    routineId: Long
) : ViewModel() {

    val routine: LiveData<Routine> = routineRepo.getRoutineById(routineId)
    val workouts: LiveData<List<Workout>> = workoutRepo.getAllWorkoutByRoutineId(routineId)

    suspend fun addWorkoutSql(workout: Workout) {
        workoutRepo.insertWorkout(workout)
    }

}