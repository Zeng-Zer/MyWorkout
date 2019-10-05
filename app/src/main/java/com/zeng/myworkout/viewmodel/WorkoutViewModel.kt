package com.zeng.myworkout.viewmodel

import androidx.lifecycle.*
import com.zeng.myworkout.model.Load
import com.zeng.myworkout.model.Workout
import com.zeng.myworkout.model.WorkoutExercise
import com.zeng.myworkout.model.WorkoutExerciseDetail
import com.zeng.myworkout.repository.WorkoutRepository
import kotlinx.coroutines.launch

class WorkoutViewModel(
    private val workoutRepository: WorkoutRepository,
    val workoutId: Long? = null
) : ViewModel() {

    private val liveDataId = MutableLiveData<Long?>(workoutId)

    val workout: LiveData<Workout> = Transformations.switchMap(liveDataId) { it?.let { id ->
        workoutRepository.getWorkoutById(id)
    }}

    val exercises: LiveData<List<WorkoutExerciseDetail>> = Transformations.switchMap(liveDataId) { it?.let { id ->
        workoutRepository.getAllWorkoutExerciseById(id)
    }}

    fun setWorkoutId(workoutId: Long) {
        liveDataId.value = workoutId
    }

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