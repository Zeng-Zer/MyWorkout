package com.zeng.myworkout.viewmodel

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
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

    val sessionWorkout = Transformations.switchMap(user) { user ->
        user?.sessionWorkoutId?.let { workoutRepo.getWorkoutById(it) }
    }

    val routine = Transformations.switchMap(workoutReference) { workout ->
        workout?.routineId?.let { routineRepo.getRoutineById(it) }
    }

    fun updateUserSessionWorkout(sessionWorkoutId: Long?) {
        viewModelScope.launch {
            workoutRepo.updateUserSessionWorkout(sessionWorkoutId)
        }
    }

    fun continueRoutineWorkout() {
        viewModelScope.launch {
            val newWorkout = workoutReference.value?.copy(id = null, reference = false)!!
            newWorkout.id = workoutRepo.insertWorkout(newWorkout)

            val newWorkoutExercises =
                workoutRepo.allWorkoutExerciseById(user.value?.workoutReferenceId!!)
                    .map { ex ->
                        ex.id = null
                        ex.workoutId = newWorkout.id
                        ex
                    }

            workoutRepo.insertWorkoutExercise(newWorkoutExercises)
            workoutRepo.updateUserSessionWorkout(newWorkout.id)
        }
    }

}