package com.zeng.myworkout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeng.myworkout.model.Workout
import com.zeng.myworkout.model.WorkoutExercise
import com.zeng.myworkout.model.WorkoutExerciseDetail
import com.zeng.myworkout.repository.WorkoutRepository
import kotlinx.coroutines.launch

class WorkoutViewModel(
    private val workoutRepository: WorkoutRepository,
    val workoutId: Long
) : ViewModel() {

    val workout: LiveData<Workout> = workoutRepository.getWorkoutById(workoutId)
    val exercises: LiveData<List<WorkoutExerciseDetail>> = workoutRepository.getAllWorkoutExerciseById(workoutId)

    fun updateWorkoutExercise(exercise: WorkoutExercise) {
        viewModelScope.launch {
            workoutRepository.updateWorkoutExercise(exercise)
        }
    }

    fun updateAllWorkoutExercise(exercises: List<WorkoutExercise>) {
        viewModelScope.launch {
            workoutRepository.updateAllWorkoutExercise(exercises)
        }
    }

    fun insertWorkoutExercise(exercises: List<WorkoutExercise>) {
        viewModelScope.launch {
            workoutRepository.insertWorkoutExercise(exercises)
        }
    }

}