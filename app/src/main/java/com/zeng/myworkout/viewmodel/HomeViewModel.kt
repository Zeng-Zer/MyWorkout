package com.zeng.myworkout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zeng.myworkout.database.WorkoutRepository
import com.zeng.myworkout.model.User

class HomeViewModel(private val repository: WorkoutRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    val text: LiveData<String> = _text
    val workout: LiveData<User> = repository.currentUser

    suspend fun changeWorkout(workoutId: Long) {
        repository.changeWorkout(workoutId)
    }

}