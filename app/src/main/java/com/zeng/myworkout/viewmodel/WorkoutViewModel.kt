package com.zeng.myworkout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeng.myworkout.model.Load
import com.zeng.myworkout.model.WorkoutExercise
import com.zeng.myworkout.model.WorkoutExerciseDetail
import com.zeng.myworkout.repository.WorkoutRepository
import kotlinx.coroutines.launch

class WorkoutViewModel(
    val workoutId: Long,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    val exercises: LiveData<List<WorkoutExerciseDetail>> = workoutRepository.getAllWorkoutExerciseById(workoutId)

    fun updateAllWorkoutExercise(exercises: List<WorkoutExercise>) {
        viewModelScope.launch {
            workoutRepository.updateAllWorkoutExercise(exercises)
        }
    }

    fun insertWorkoutExerciseWithLoads(exercisesWithLoads: List<Pair<WorkoutExercise, List<Load>>>) {
        viewModelScope.launch {
            val exercises = exercisesWithLoads.map { it.first }
            val loadsWithoutIds = exercisesWithLoads.map { it.second }

            val ids = workoutRepository.insertWorkoutExercise(exercises)

            // Add workout exercise id to loads
            val loads = loadsWithoutIds
                .zip(ids)
                .flatMap { (loads, id) ->
                    loads.map { load ->
                        load.workoutExerciseId = id
                        load
                    }
                }
            workoutRepository.insertLoad(loads)
        }
    }

}