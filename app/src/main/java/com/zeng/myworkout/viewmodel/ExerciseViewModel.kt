package com.zeng.myworkout.viewmodel

import androidx.lifecycle.*
import com.zeng.myworkout.model.Category
import com.zeng.myworkout.model.Exercise
import com.zeng.myworkout.repository.ExerciseRepository
import kotlinx.coroutines.launch

class ExerciseViewModel(private val repository: ExerciseRepository) : ViewModel() {

    val exercises: LiveData<List<Exercise>> = repository.getAllExercise()
    var exercisesToAdd: MutableLiveData<List<Exercise>> = MutableLiveData(emptyList())
    val hasChecked: LiveData<Boolean> = exercisesToAdd.map { !it.isNullOrEmpty() }

    suspend fun getCategories(): List<Category> = repository.getAllCategory()

    fun insertExercise(exercise: Exercise) {
        viewModelScope.launch {
            repository.addExercise(exercise)
        }
    }

    fun updateExercise(exercise: Exercise) {
        viewModelScope.launch {
            repository.updateExercise(exercise)
        }
    }
}