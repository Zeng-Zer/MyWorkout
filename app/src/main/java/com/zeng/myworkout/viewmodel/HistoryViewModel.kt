package com.zeng.myworkout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.zeng.myworkout.model.WorkoutWithExercises
import com.zeng.myworkout.repository.RoutineRepository
import com.zeng.myworkout.repository.WorkoutRepository

class HistoryViewModel(
    private val routineRepo: RoutineRepository,
    private val workoutRepo: WorkoutRepository
) : ViewModel() {

    val workouts: LiveData<List<WorkoutWithExercises>> = workoutRepo.getAllFinishedWorkout().map { workouts ->
        workouts.map {
            val routine = it.routineId?.let { id -> routineRepo.routineById(id) }?.name
            WorkoutWithExercises(it, workoutRepo.allWorkoutExerciseById(it.id!!), routine)
        }
    }

}