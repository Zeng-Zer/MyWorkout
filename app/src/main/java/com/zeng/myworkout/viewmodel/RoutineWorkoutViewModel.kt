package com.zeng.myworkout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeng.myworkout.model.Load
import com.zeng.myworkout.model.WorkoutExercise
import com.zeng.myworkout.model.WorkoutExerciseDetail
import com.zeng.myworkout.repository.WorkoutRepository
import kotlinx.coroutines.launch

class RoutineWorkoutViewModel( workoutId: Long, private val workoutRepo: WorkoutRepository): ViewModel() {
    val exercises: LiveData<List<WorkoutExerciseDetail>> = workoutRepo.getAllWorkoutExerciseById(workoutId)

    fun deleteWorkoutExercise(exercise: WorkoutExercise) {
        viewModelScope.launch {
            workoutRepo.deleteWorkoutExercise(exercise)
        }
    }

    fun updateAllWorkoutExercise(list: List<WorkoutExercise>) {
        viewModelScope.launch {
            workoutRepo.updateAllWorkoutExercise(list)
        }
    }

    fun updateLoad(load: Load) {
        viewModelScope.launch {
            workoutRepo.updateLoad(load)
        }
    }

    fun deleteLoad(load: Load) {
        viewModelScope.launch {
            workoutRepo.deleteLoad(load)
        }
    }

    fun insertLoad(load: Load) {
        viewModelScope.launch {
            workoutRepo.insertLoad(load)
        }
    }
}