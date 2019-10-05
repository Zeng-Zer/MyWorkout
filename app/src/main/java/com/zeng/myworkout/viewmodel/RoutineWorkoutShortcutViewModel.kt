package com.zeng.myworkout.viewmodel

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import com.zeng.myworkout.repository.WorkoutRepository

class RoutineWorkoutShortcutViewModel(
    private val workoutRepository: WorkoutRepository,
    private val routineId: Long
) : ViewModel() {

    val workouts = workoutRepository.getAllWorkoutByRoutineId(routineId)
    val hasWorkout = Transformations.map(workouts) { !it.isNullOrEmpty() }.distinctUntilChanged()
}