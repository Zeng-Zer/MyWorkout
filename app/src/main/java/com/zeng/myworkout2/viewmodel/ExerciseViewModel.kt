package com.zeng.myworkout2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zeng.myworkout2.database.ExerciseRepository
import com.zeng.myworkout2.model.Exercise

class ExerciseViewModel(private val repository: ExerciseRepository) : ViewModel() {

    val exercises: LiveData<List<Exercise>> = repository.getAllExercise()

}