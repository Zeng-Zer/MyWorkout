package com.zeng.myworkout.repository

import androidx.lifecycle.LiveData
import com.zeng.myworkout.database.LoadDao
import com.zeng.myworkout.database.UserDao
import com.zeng.myworkout.database.WorkoutDao
import com.zeng.myworkout.database.WorkoutExerciseDao
import com.zeng.myworkout.model.*

class WorkoutRepository private constructor(
    private val workoutDao: WorkoutDao,
    private val workoutExerciseDao: WorkoutExerciseDao,
    private val loadDao: LoadDao,
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
    fun getAllWorkoutByRoutineId(routineId: Long): LiveData<List<Workout>> = workoutDao.getAllReferenceWorkoutByRoutineId(routineId)

    suspend fun insertWorkout(workout: Workout) = workoutDao.insert(workout)

    suspend fun insertWorkoutExercise(exercises: List<WorkoutExercise>) = workoutExerciseDao.insert(exercises)
    suspend fun updateWorkoutExercise(exercise: WorkoutExercise) = workoutExerciseDao.update(exercise)
    suspend fun updateAllWorkoutExercise(exercises: List<WorkoutExercise>) = workoutExerciseDao.update(exercises)
    suspend fun deleteWorkoutExercise(exercise: WorkoutExercise) = workoutExerciseDao.delete(exercise)


    fun getAllLoadById(workoutExerciseId: Long): LiveData<List<Load>> = loadDao.getAllLoadById(workoutExerciseId)
    suspend fun insertLoad(loads: List<Load>) = loadDao.insert(loads)
    suspend fun insertLoad(load: Load) = loadDao.insert(load)
    suspend fun updateLoad(load: Load) = loadDao.update(load)
    suspend fun deleteLoad(load: Load) = loadDao.delete(load)

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: WorkoutRepository? = null

        fun getInstance(workoutDao: WorkoutDao, workoutExerciseDao: WorkoutExerciseDao, loadDao: LoadDao, userDao: UserDao) =
            instance ?: synchronized(this) {
                instance
                    ?: WorkoutRepository(
                        workoutDao,
                        workoutExerciseDao,
                        loadDao,
                        userDao
                    ).also { instance = it }
            }
    }
}