package com.zeng.myworkout.viewmodel

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.zeng.myworkout.repository.WorkoutRepository
import kotlinx.coroutines.launch

class RoutineWorkoutShortcutViewModel(
    private val workoutRepository: WorkoutRepository,
    private val routineId: Long
) : ViewModel() {

    val workouts = workoutRepository.getAllWorkoutByRoutineId(routineId)
    val hasWorkout = Transformations.map(workouts) { !it.isNullOrEmpty() }.distinctUntilChanged()

    fun updateUserWorkout(workoutId: Long) {
        viewModelScope.launch {
            workoutRepository.updateUserWorkout(workoutId)
        }
    }
}