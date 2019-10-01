package com.zeng.myworkout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeng.myworkout.repository.WorkoutRepository
import com.zeng.myworkout.model.Exercise
import com.zeng.myworkout.model.WorkoutExercise
import com.zeng.myworkout.model.WorkoutExerciseSql
import com.zeng.myworkout.model.WorkoutSql
import kotlinx.coroutines.launch

class WorkoutViewModel(
    private val workoutRepository: WorkoutRepository,
    val workoutId: Long
) : ViewModel() {

    val workout: LiveData<WorkoutSql> = workoutRepository.getWorkoutSqlById(workoutId)
    val exercises: LiveData<List<WorkoutExercise>> = workoutRepository.getAllWorkoutExerciseById(workoutId)

    fun updateWorkoutExerciseSql(exercise: WorkoutExerciseSql) {
        viewModelScope.launch {
            workoutRepository.updateWorkoutExerciseSql(exercise)
        }
    }

    fun updateAllWorkoutExerciseSql(exercises: List<WorkoutExerciseSql>) {
        viewModelScope.launch {
            workoutRepository.updateAllWorkoutExerciseSql(exercises)
        }
    }

    fun insertWorkoutExerciseSql(exercise: WorkoutExerciseSql) {
        viewModelScope.launch {
            workoutRepository.insertWorkoutExerciseSql(exercise)
        }
    }

    fun insertWorkoutExerciseSql(exercises: List<WorkoutExerciseSql>) {
        viewModelScope.launch {
            workoutRepository.insertWorkoutExerciseSql(exercises)
        }
    }

    fun insertExercise(exercise: Exercise) {
        viewModelScope.launch {
            workoutRepository.insertExercise(exercise)
        }
    }
}