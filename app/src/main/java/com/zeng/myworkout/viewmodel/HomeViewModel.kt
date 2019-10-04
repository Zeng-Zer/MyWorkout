package com.zeng.myworkout.viewmodel

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.zeng.myworkout.repository.RoutineRepository
import com.zeng.myworkout.repository.WorkoutRepository

class HomeViewModel(
    private val routineRepo: RoutineRepository,
    private val workoutRepo: WorkoutRepository
) : ViewModel() {

    val user = workoutRepo.getCurrentUser()

    val workout = Transformations.switchMap(user) { user ->
        user?.workoutId?.let { workoutRepo.getWorkoutById(it) }
    }

    val routine = Transformations.switchMap(workout) { workout ->
        workout?.routineId?.let { routineRepo.getRoutineById(it) }
    }

}