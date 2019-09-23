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

    fun getWorkoutSqlById(workoutId: Long): LiveData<WorkoutSql> {
        return workoutDao.getWorkoutSqlById(workoutId)
    }

    fun getAllWorkoutExerciseById(workoutId: Long): LiveData<List<WorkoutExercise>> {
        return workoutDao.getAllWorkoutExerciseById(workoutId)
    }

    fun getAllWorkoutSqlByRoutineId(routineId: Long): LiveData<List<WorkoutSql>> {
        return workoutDao.getAllWorkoutSqlByRoutineId(routineId)
    }

    suspend fun insertWorkoutSql(workout: WorkoutSql) {
        workoutDao.insertWorkoutSql(workout)
    }

    suspend fun insertExercise(exercise: Exercise) {
        exercise.id = exerciseDao.insert(exercise)
    }

    suspend fun insertWorkoutExerciseSql(exercise: WorkoutExerciseSql) {
        exerciseDao.insertWorkoutExerciseSql(exercise)
    }

    suspend fun updateWorkoutExerciseSql(exercise: WorkoutExerciseSql) {
        exerciseDao.updateWorkoutExerciseSql(exercise)
    }

    suspend fun updateAllWorkoutExerciseSql(exercises: List<WorkoutExerciseSql>) {
        exerciseDao.updateAllWorkoutExerciseSql(exercises)
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