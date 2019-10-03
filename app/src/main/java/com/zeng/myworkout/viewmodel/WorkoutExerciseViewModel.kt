package com.zeng.myworkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeng.myworkout.model.Load
import com.zeng.myworkout.model.WorkoutExercise
import com.zeng.myworkout.repository.WorkoutRepository
import kotlinx.coroutines.launch

class WorkoutExerciseViewModel(
    private val workoutRepository: WorkoutRepository,
    private val workoutExerciseId: Long
) : ViewModel() {

    val loads = workoutRepository.getAllLoadById(workoutExerciseId)

    fun updateLoad(load: Load) {
        viewModelScope.launch {
            workoutRepository.updateLoad(load)
        }
    }

    fun insertLoad(load: Load) {
        viewModelScope.launch {
            workoutRepository.insertLoad(load)
        }
    }

    fun deleteLoad(load: Load) {
        viewModelScope.launch {
            workoutRepository.deleteLoad(load)
        }
    }

    fun deleteWorkoutExercise(exercise: WorkoutExercise) {
        viewModelScope.launch {
            workoutRepository.deleteWorkoutExercise(exercise)
        }
    }
}