package com.zeng.myworkout.repository

import androidx.lifecycle.LiveData
import com.zeng.myworkout.database.UserDao
import com.zeng.myworkout.database.WorkoutDao
import com.zeng.myworkout.database.WorkoutExerciseDao
import com.zeng.myworkout.model.User
import com.zeng.myworkout.model.Workout
import com.zeng.myworkout.model.WorkoutExercise
import com.zeng.myworkout.model.WorkoutExerciseDetail

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
    fun getAllWorkoutExerciseById(workoutId: Long): LiveData<List<WorkoutExerciseDetail>> = workoutDao.getAllWorkoutExerciseById(workoutId)
    fun getAllWorkoutByRoutineId(routineId: Long): LiveData<List<Workout>> = workoutDao.getAllWorkoutByRoutineId(routineId)

    suspend fun insertWorkout(workout: Workout) = workoutDao.insert(workout)

    suspend fun insertWorkoutExercise(exercises: List<WorkoutExercise>) = workoutExerciseDao.insert(exercises)
    suspend fun updateWorkoutExercise(exercise: WorkoutExercise) = workoutExerciseDao.update(exercise)
    suspend fun updateAllWorkoutExercise(exercises: List<WorkoutExercise>) = workoutExerciseDao.update(exercises)

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