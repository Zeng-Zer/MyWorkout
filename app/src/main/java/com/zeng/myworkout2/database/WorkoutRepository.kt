package com.zeng.myworkout2.database

import androidx.lifecycle.LiveData
import com.zeng.myworkout2.model.*

class WorkoutRepository private constructor(
    private val workoutDao: WorkoutDao,
    private val exerciseDao: ExerciseDao,
    private val userDao: UserDao
) {
    val currentUser: LiveData<User> = userDao.getCurrentUser()

    suspend fun changeWorkout(workoutId: Long) {
        val user = currentUser.value!!
        user.workoutId = workoutId
        userDao.update(user)
    }

    fun getWorkoutById(workoutId: Long): LiveData<Workout> {
        return workoutDao.getWorkoutById(workoutId)
    }

    suspend fun insertWorkoutSql(workout: WorkoutSql) {
        workoutDao.insertWorkoutSql(workout)
    }

    suspend fun updateWorkoutExerciseSql(exercise: WorkoutExerciseSql) {
        exerciseDao.updateWorkoutExerciseSql(exercise)
    }

    suspend fun insertWorkoutExerciseSql(exercise: WorkoutExerciseSql) {
        exerciseDao.insertWorkoutExerciseSql(exercise)
    }

    suspend fun insertExercise(exercise: Exercise) {
        exercise.id = exerciseDao.insert(exercise)
    }

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: WorkoutRepository? = null

        fun getInstance(workoutDao: WorkoutDao, exerciseDao: ExerciseDao, userDao: UserDao) =
            instance ?: synchronized(this) {
                instance ?: WorkoutRepository(workoutDao, exerciseDao, userDao).also { instance = it }
            }
    }
}