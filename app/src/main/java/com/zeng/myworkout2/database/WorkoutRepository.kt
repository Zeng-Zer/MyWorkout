package com.zeng.myworkout2.database

import androidx.lifecycle.LiveData
import com.zeng.myworkout2.model.User
import com.zeng.myworkout2.model.Workout

class WorkoutRepository private constructor(private val workoutDao: WorkoutDao, private val userDao: UserDao) {

    val currentUser: LiveData<User> = userDao.getCurrentUser()

    suspend fun changeWorkout(workoutId: Long) {
        val user = currentUser.value!!
        user.workoutId = workoutId
        userDao.update(user)
    }

    fun getWorkoutById(workoutId: Long): LiveData<Workout> {
        return workoutDao.getWorkoutById(workoutId)
    }

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: WorkoutRepository? = null

        fun getInstance(workoutDao: WorkoutDao, userDao: UserDao) =
            instance ?: synchronized(this) {
                instance ?: WorkoutRepository(workoutDao, userDao).also { instance = it }
            }
    }
}