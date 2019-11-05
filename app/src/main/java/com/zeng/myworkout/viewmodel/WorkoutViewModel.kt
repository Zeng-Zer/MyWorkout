package com.zeng.myworkout.viewmodel

import androidx.lifecycle.*
import com.zeng.myworkout.model.WorkoutExercise
import com.zeng.myworkout.model.WorkoutExerciseDetail
import com.zeng.myworkout.repository.WorkoutRepository
import kotlinx.coroutines.launch

class WorkoutViewModel(private val workoutRepo: WorkoutRepository): ViewModel() {

    val workoutId: MutableLiveData<Long?> = MutableLiveData(null)
    val exercises: LiveData<List<WorkoutExerciseDetail>> = workoutId.switchMap { id ->
        if (id != null) {
            workoutRepo.getAllWorkoutExerciseById(id)
        } else {
            MutableLiveData()
        }
    }

    fun deleteWorkoutExercise(exercise: WorkoutExercise) {
        viewModelScope.launch {
            workoutRepo.deleteWorkoutExercise(exercise)
        }
    }

    fun updateWorkoutExercise(exercise: WorkoutExercise) {
        viewModelScope.launch {
            workoutRepo.updateWorkoutExercise(exercise)
        }
    }

    fun updateWorkoutExercise(list: List<WorkoutExercise>) {
        viewModelScope.launch {
            workoutRepo.updateWorkoutExercise(list)
        }
    }

    fun insertWorkoutExercises(exercises: List<WorkoutExercise>) {
        viewModelScope.launch {
            workoutRepo.insertWorkoutExercise(exercises)
        }
    }

}