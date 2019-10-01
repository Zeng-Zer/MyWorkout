package com.zeng.myworkout.repository

import androidx.lifecycle.LiveData
import com.zeng.myworkout.database.UserDao
import com.zeng.myworkout.database.WorkoutDao
import com.zeng.myworkout.database.WorkoutExerciseDao
import com.zeng.myworkout.model.*

class WorkoutRepository private constructor(
    private val workoutDao: WorkoutDao,
    private val workoutExerciseDao: WorkoutExerciseDao,
    private val userDao: UserDao
) {
    val currentUser: LiveData<User> = userDao.getCurrentUser()

    suspend fun changeWorkout(workoutId: Long) {
        val user = currentUser.value!!
        user.workoutId = workoutId
        userDao.update(user)
    }

    fun getWorkoutById(workoutId: Long): LiveData<Workout> = workoutDao.getWorkoutById(workoutId)
    fun getAllWorkoutExerciseById(workoutId: Long): LiveData<List<WorkoutExercise>> = workoutDao.getAllWorkoutExerciseById(workoutId)
    fun getAllWorkoutByRoutineId(routineId: Long): LiveData<List<Workout>> = workoutDao.getAllWorkoutByRoutineId(routineId)

    suspend fun insertWorkout(workout: Workout) = workoutDao.insert(workout)

    suspend fun insertExercise(exercise: Exercise) {
        exercise.id = workoutExerciseDao.insertExercise(exercise)
    }

    suspend fun insertWorkoutExerciseSql(exercise: WorkoutExerciseSql) = workoutExerciseDao.insert(exercise)
    suspend fun insertWorkoutExerciseSql(exercises: List<WorkoutExerciseSql>) = workoutExerciseDao.insert(exercises)
    suspend fun updateWorkoutExerciseSql(exercise: WorkoutExerciseSql) = workoutExerciseDao.update(exercise)
    suspend fun updateAllWorkoutExerciseSql(exercises: List<WorkoutExerciseSql>) = workoutExerciseDao.update(exercises)

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: WorkoutRepository? = null

        fun getInstance(workoutDao: WorkoutDao, workoutExerciseDao: WorkoutExerciseDao, userDao: UserDao) =
            instance ?: synchronized(this) {
                instance
                    ?: WorkoutRepository(
                        workoutDao,
                        workoutExerciseDao,
                        userDao
                    ).also { instance = it }
            }
    }
}