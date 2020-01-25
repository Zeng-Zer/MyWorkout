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

    // Grouped by routine name and workout name
    val groupedWorkouts: LiveData<List<GroupedWorkouts>> = workouts.map { workouts ->
        workouts.groupBy { w -> w.routine to w.workout.name }.toList()
    }

}

typealias RoutineWorkoutName = Pair<String?, String>
typealias GroupedWorkouts = Pair<RoutineWorkoutName, List<WorkoutWithExercises>>
