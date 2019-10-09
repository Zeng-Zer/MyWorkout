package com.zeng.myworkout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeng.myworkout.model.*
import com.zeng.myworkout.repository.RoutineRepository
import com.zeng.myworkout.repository.WorkoutRepository
import kotlinx.coroutines.launch

class RoutineDetailViewModel(
    routineId: Long,
    private val routineRepo: RoutineRepository,
    private val workoutRepo: WorkoutRepository
) : ViewModel() {

    val routine: LiveData<Routine> = routineRepo.getRoutineById(routineId)
    val workouts: LiveData<List<WorkoutName>> = workoutRepo.getAllReferenceWorkoutNamesByRoutineId(routineId)

    fun insertWorkout(workout: Workout) {
        viewModelScope.launch {
            workoutRepo.insertWorkout(workout)
        }
    }

    fun deleteWorkoutById(workoutId: Long) {
        viewModelScope.launch {
            workoutRepo.deleteWorkoutReorderById(workoutId)
        }
    }

    fun insertWorkoutExerciseWithLoads(exercisesWithLoads: List<Pair<WorkoutExercise, List<Load>>>) {
        viewModelScope.launch {
            val exercises = exercisesWithLoads.map { it.first }
            val loadsWithoutIds = exercisesWithLoads.map { it.second }

            val ids = workoutRepo.insertWorkoutExercise(exercises)

            // Add workout exercise id to loads
            val loads = loadsWithoutIds
                .zip(ids)
                .flatMap { (loads, id) ->
                    loads.map { load ->
                        load.workoutExerciseId = id
                        load
                    }
                }
            workoutRepo.insertLoad(loads)
        }
    }

}