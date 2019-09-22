package com.zeng.myworkout2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeng.myworkout2.database.WorkoutRepository
import com.zeng.myworkout2.model.Workout
import com.zeng.myworkout2.model.WorkoutExerciseSql
import kotlinx.coroutines.launch

class WorkoutViewModel(
    private val workoutRepository: WorkoutRepository,
    workoutId: Long
) : ViewModel() {

    val workout: LiveData<Workout> = workoutRepository.getWorkoutById(workoutId)

    fun updateWorkoutExerciseSql(exercise: WorkoutExerciseSql) {
        viewModelScope.launch {
            workoutRepository.updateWorkoutExerciseSql(exercise)
        }
    }
}