package com.zeng.myworkout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zeng.myworkout.model.Exercise
import com.zeng.myworkout.repository.ExerciseRepository

class ExerciseViewModel(private val repository: ExerciseRepository) : ViewModel() {

    val exercises: LiveData<List<Exercise>> = repository.getAllExercise()

    var checkedList: MutableList<Exercise> = mutableListOf()

    val hasChecked: MutableLiveData<Boolean> = MutableLiveData(false)

}