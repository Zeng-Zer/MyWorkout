package com.zeng.myworkout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeng.myworkout.model.Routine
import com.zeng.myworkout.model.RoutineWithWorkouts
import com.zeng.myworkout.model.User
import com.zeng.myworkout.model.Workout
import com.zeng.myworkout.repository.RoutineRepository
import com.zeng.myworkout.repository.WorkoutRepository
import kotlinx.coroutines.launch

class RoutineViewModel(
    private val routineRepo: RoutineRepository,
    private val workoutRepo: WorkoutRepository
) : ViewModel() {

    val routines: LiveData<List<RoutineWithWorkouts>> = routineRepo.getAllRoutineWithWorkouts()

    suspend fun getUser(): User? = workoutRepo.currentUser()

    suspend fun insertRoutineAsync(routine: Routine) : Long {
        return routineRepo.insertRoutine(routine)
    }

    fun deleteRoutine(routine: Routine) {
        viewModelScope.launch {
            routineRepo.deleteRoutine(routine)
        }
    }

    fun updateRoutine(routines: List<Routine>) {
        viewModelScope.launch {
            routineRepo.update(routines)
        }
    }

    fun updateUserWorkout(workout: Workout) {
        viewModelScope.launch {
            // update user workout reference
            workoutRepo.updateUserWorkoutReference(workout.id!!)

            // create user workout session
            val newWorkout = workoutRepo.newWorkoutSessionFromReference(workout)
            workoutRepo.updateUserWorkoutSession(newWorkout.id)
        }
    }

    fun deleteWorkout(workoutId: Long) {
        viewModelScope.launch {
            workoutRepo.deleteWorkoutById(workoutId)
        }
    }
}