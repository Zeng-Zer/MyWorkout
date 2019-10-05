package com.zeng.myworkout.viewmodel

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.zeng.myworkout.repository.RoutineRepository
import com.zeng.myworkout.repository.WorkoutRepository
import kotlinx.coroutines.launch

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

    val sessionWorkout = Transformations.switchMap(user) { user ->
        user?.sessionWorkoutId?.let { workoutRepo.getWorkoutById(it) }
    }.distinctUntilChanged()

    fun updateUserSessionWorkout(sessionWorkoutId: Long?) {
        viewModelScope.launch {
            workoutRepo.updateUserSessionWorkout(sessionWorkoutId)
        }
    }

    // Create a Workout that will be the session workout
    fun continueRoutineWorkout() {
        viewModelScope.launch {
            val newWorkout = workoutReference.value?.copy(id = null, reference = false)!!
            newWorkout.id = workoutRepo.insertWorkout(newWorkout)

            val referenceExercises = workoutRepo.allWorkoutExerciseById(user.value?.workoutReferenceId!!)

            val referenceExerciseIds = referenceExercises.map { it.id!! }

            val newWorkoutExercises = referenceExercises
                    .map { ex ->
                        ex.id = null
                        ex.workoutId = newWorkout.id
                        ex
                    }

            val exerciseIds = workoutRepo.insertWorkoutExercise(newWorkoutExercises)

            // Insert loads
            val loads = referenceExerciseIds.zip(exerciseIds)
                .flatMap { (referenceId, newId) ->
                    workoutRepo.allLoadById(referenceId)
                        .map { it.copy(id = null, workoutExerciseId = newId) }
                }
            workoutRepo.insertLoad(loads)

            workoutRepo.updateUserSessionWorkout(newWorkout.id)
        }
    }

}