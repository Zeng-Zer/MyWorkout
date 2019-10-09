package com.zeng.myworkout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zeng.myworkout.model.WorkoutExerciseDetail
import com.zeng.myworkout.repository.WorkoutRepository

class RoutineWorkoutViewModel(
    workoutId: Long,
    private val workoutRepo: WorkoutRepository
): ViewModel() {
    val exercises: LiveData<List<WorkoutExerciseDetail>> = workoutRepo.getAllWorkoutExerciseById(workoutId)
}