package com.zeng.myworkout2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeng.myworkout2.database.WorkoutRepository
import com.zeng.myworkout2.model.Exercise
import com.zeng.myworkout2.model.WorkoutExercise
import com.zeng.myworkout2.model.WorkoutExerciseSql
import com.zeng.myworkout2.model.WorkoutSql
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

    fun insertExercise(exercise: Exercise) {
        viewModelScope.launch {
            workoutRepository.insertExercise(exercise)
        }
    }

    // TODO TEST FUNCTION TO BE REMOVED

    fun insertExerciceTest(exercise: Exercise, workoutExercise: WorkoutExerciseSql) {
        viewModelScope.launch {
            workoutRepository.insertExercise(exercise)
            workoutExercise.exerciseId = exercise.id
            workoutRepository.insertWorkoutExerciseSql(workoutExercise)
        }
    }
}