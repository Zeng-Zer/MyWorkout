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

    val sessionWorkout = Transformations.switchMap(user) { user ->
        user?.sessionWorkoutId?.let { workoutRepo.getWorkoutById(it) }
    }

    fun deleteSessionWorkout() {
        viewModelScope.launch {
            workoutRepo.deleteWorkout(sessionWorkout.value!!)
        }
    }

    fun updateUserSessionWorkout(sessionWorkoutId: Long?) {
        viewModelScope.launch {
            workoutRepo.updateUserSessionWorkout(sessionWorkoutId)
        }
    }

    fun finishCurrentSessionWorkout() {
        sessionWorkout.value?.let { workout ->
            workout.finishDate = Date()
            viewModelScope.launch {
                workoutRepo.updateWorkout(workout)
            }
        }
    }

    // TODO Probably need to refactor this
    fun updateToNextWorkout() {
        sessionWorkout.value?.let { sessionWorkout ->
            viewModelScope.launch {
                val routineId = sessionWorkout.routineId!!
                val count = routineRepo.getReferenceWorkoutCount(routineId)
                var newWorkoutOrderId = 0
                if (sessionWorkout.order.toLong() < count - 1) {
                    newWorkoutOrderId = sessionWorkout.order + 1
                }
                val newWorkout = workoutRepo.getWorkoutByRoutineOrder(routineId, newWorkoutOrderId)
                workoutRepo.updateUserWorkout(newWorkout.id!!)
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
            workoutRepo.updateUserSessionWorkout(newWorkout.id)
        }
    }

}