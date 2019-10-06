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
            val newWorkout = workoutReference.value?.copy(id = null, reference = false, startDate = Date())!!
            newWorkout.id = workoutRepo.insertWorkout(newWorkout)

            val referenceExercises = workoutRepo.allWorkoutExerciseById(user.value?.workoutReferenceId!!)
            val referenceExerciseIds = referenceExercises.map { it.id!! }
            val newWorkoutExercises = referenceExercises
                    .map { ex ->
                        ex.id = null
                        ex.workoutId = newWorkout.id
                        ex
                    }


            // Insert loads with ids
            val exerciseIds = workoutRepo.insertWorkoutExercise(newWorkoutExercises)
            val loads = referenceExerciseIds.zip(exerciseIds)
                .flatMap { (referenceId, newId) ->
                    workoutRepo.allLoadById(referenceId)
                        .map { it.copy(id = null, workoutExerciseId = newId) }
                }

            workoutRepo.insertLoad(loads)
            workoutRepo.updateUserWorkoutSession(newWorkout.id)
        }
    }

}