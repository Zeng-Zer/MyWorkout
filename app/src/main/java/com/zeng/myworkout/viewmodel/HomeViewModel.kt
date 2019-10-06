package com.zeng.myworkout.viewmodel

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeng.myworkout.repository.RoutineRepository
import com.zeng.myworkout.repository.WorkoutRepository
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel(
    private val routineRepo: RoutineRepository,
    private val workoutRepo: WorkoutRepository
) : ViewModel() {

    val user = workoutRepo.getCurrentUser()

    val workoutReference = Transformations.switchMap(user) { user ->
        user?.workoutReferenceId?.let { workoutRepo.getWorkoutById(it) }
    }

    val routine = Transformations.switchMap(workoutReference) { workout ->
        workout?.routineId?.let { routineRepo.getRoutineById(it) }
    }

    val workoutSession = Transformations.switchMap(user) { user ->
        user?.workoutSessionId?.let { workoutRepo.getWorkoutById(it) }
    }

    fun deleteWorkoutSession() {
        viewModelScope.launch {
            workoutRepo.deleteWorkout(workoutSession.value!!)
        }
    }

    fun finishCurrentWorkoutSession() {
        workoutSession.value?.let { workout ->
            workout.finishDate = Date()
            viewModelScope.launch {
                workoutRepo.updateWorkout(workout)
                workoutRepo.updateUserWorkoutSession(null)
            }
        }
    }

    fun updateToNextWorkout() {
        workoutSession.value?.let { workoutSession ->
            viewModelScope.launch {
                val routineId = workoutSession.routineId!!
                val workouts = workoutRepo.allReferenceWorkoutByRoutineId(routineId)

                // Cycle order
                var newWorkoutOrderId = 0
                if (workoutSession.order < workouts.size - 1) {
                    newWorkoutOrderId = workoutSession.order + 1
                }

                workoutRepo.updateUserWorkoutReference(workouts[newWorkoutOrderId].id!!)
            }
        }
    }

    // Create a Workout that will be the session workout
    fun continueRoutineWorkout() {
        viewModelScope.launch {
            val newWorkout = workoutRepo.newWorkoutSessionFromReference(workoutReference.value!!)
            workoutRepo.updateUserWorkoutSession(newWorkout.id)
        }
    }

}