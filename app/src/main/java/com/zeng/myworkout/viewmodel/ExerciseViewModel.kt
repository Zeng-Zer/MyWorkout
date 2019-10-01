package com.zeng.myworkout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zeng.myworkout.repository.ExerciseRepository
import com.zeng.myworkout.model.Exercise

class ExerciseViewModel(private val repository: ExerciseRepository) : ViewModel() {

    val exercises: LiveData<List<Exercise>> = repository.getAllExercise()

}