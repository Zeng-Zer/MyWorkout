package com.zeng.myworkout.viewmodel

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.zeng.myworkout.model.Workout
import com.zeng.myworkout.repository.WorkoutRepository
import kotlinx.coroutines.launch

class RoutineWorkoutShortcutViewModel(
    private val workoutRepository: WorkoutRepository,
    private val routineId: Long
) : ViewModel() {

    val workouts = workoutRepository.getAllWorkoutByRoutineId(routineId)
    val hasWorkout = Transformations.map(workouts) { !it.isNullOrEmpty() }.distinctUntilChanged()

    suspend fun getUser() = workoutRepository.currentUser()

    suspend fun updateUserWorkout(workout: Workout) {
        // update user workout reference
        workoutRepository.updateUserWorkoutReference(workout.id!!)

        // create user workout session
        val newWorkout = workoutRepository.newWorkoutSessionFromReference(workout)
        workoutRepository.updateUserWorkoutSession(newWorkout.id)
    }

    fun deleteWorkout(workoutId: Long) {
        viewModelScope.launch {
            workoutRepository.deleteWorkoutById(workoutId)
        }
    }
}