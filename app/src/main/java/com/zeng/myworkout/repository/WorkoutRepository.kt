package com.zeng.myworkout.repository

import androidx.lifecycle.LiveData
import com.zeng.myworkout.database.UserDao
import com.zeng.myworkout.database.WorkoutDao
import com.zeng.myworkout.database.WorkoutExerciseDao
import com.zeng.myworkout.model.User
import com.zeng.myworkout.model.Workout
import com.zeng.myworkout.model.WorkoutExercise
import com.zeng.myworkout.model.WorkoutExerciseDetail
import java.util.*

class WorkoutRepository (
    private val workoutDao: WorkoutDao,
    private val workoutExerciseDao: WorkoutExerciseDao,
    private val userDao: UserDao
) {
    fun getCurrentUser(): LiveData<User> = userDao.getCurrentUser()
    fun currentUser(): User? = userDao.currentUser()
    suspend fun updateUser(user: User) = userDao.update(user)
    suspend fun updateUserWorkoutReference(workoutId: Long) = userDao.updateUserWorkoutReference(workoutId)
    suspend fun updateUserWorkoutSession(sessionWorkoutId: Long?) = userDao.updateUserWorkoutSession(sessionWorkoutId)

    fun getWorkoutById(workoutId: Long): LiveData<Workout?> = workoutDao.getWorkoutById(workoutId)
    fun getAllReferenceWorkoutByRoutineId(routineId: Long): LiveData<List<Workout>> = workoutDao.getAllReferenceWorkoutByRoutineId(routineId)
    suspend fun allReferenceWorkoutByRoutineId(routineId: Long): List<Workout> = workoutDao.allReferenceWorkoutByRoutineId(routineId)

    suspend fun insertWorkout(workout: Workout) = workoutDao.insert(workout)
    suspend fun updateWorkout(workout: Workout) = workoutDao.update(workout)
    suspend fun updateWorkout(workouts: List<Workout>) = workoutDao.update(workouts)
    suspend fun deleteWorkoutReorder(workout: Workout) = workoutDao.deleteWorkoutReorder(workout)
    suspend fun deleteWorkout(workout: Workout) = workoutDao.delete(workout)
    suspend fun deleteWorkoutById(workoutId: Long) = workoutDao.deleteWorkoutById(workoutId)

    suspend fun insertWorkoutExercise(exercises: List<WorkoutExercise>) = workoutExerciseDao.insert(exercises)
    suspend fun updateWorkoutExercise(exercises: List<WorkoutExercise>) = workoutExerciseDao.update(exercises)
    suspend fun updateWorkoutExercise(exercise: WorkoutExercise) = workoutExerciseDao.update(exercise)
    suspend fun deleteWorkoutExercise(exercise: WorkoutExercise) = workoutExerciseDao.delete(exercise)

    private suspend fun allWorkoutExerciseById(workoutId: Long): List<WorkoutExerciseDetail> = workoutDao.allWorkoutExerciseById(workoutId)
    fun getAllWorkoutExerciseById(workoutId: Long): LiveData<List<WorkoutExerciseDetail>> = workoutDao.getAllWorkoutExerciseById(workoutId)

    suspend fun newWorkoutSessionFromReference(workoutReference: Workout): Workout {
        val newWorkout = workoutReference.copy(id = null, reference = false, startDate = Date())
        newWorkout.id = insertWorkout(newWorkout)

        val referenceExercises = allWorkoutExerciseById(workoutReference.id!!)
        val newWorkoutExercises = referenceExercises.map { ex ->
            ex.exercise.id = null
            ex.exercise.workoutId = newWorkout.id
            ex.exercise
        }

        insertWorkoutExercise(newWorkoutExercises)
        return newWorkout
    }
}