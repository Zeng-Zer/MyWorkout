package com.zeng.myworkout2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zeng.myworkout2.database.WorkoutRepository
import com.zeng.myworkout2.model.Workout

class WorkoutViewModel(
    private val workoutRepository: WorkoutRepository,
    workoutId: Long
) : ViewModel() {

    val workout: LiveData<Workout> = workoutRepository.getWorkoutById(workoutId)
}