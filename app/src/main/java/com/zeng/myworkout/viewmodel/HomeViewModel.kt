package com.zeng.myworkout.viewmodel

import androidx.lifecycle.*
import com.zeng.myworkout.model.User
import com.zeng.myworkout.model.Workout
import com.zeng.myworkout.repository.RoutineRepository
import com.zeng.myworkout.repository.WorkoutRepository
import java.util.*

class HomeViewModel(
    private val routineRepo: RoutineRepository,
    private val workoutRepo: WorkoutRepository
) : ViewModel() {

    val user = workoutRepo.getCurrentUser()
    fun currentUser() = workoutRepo.currentUser()

    // TODO WARNING THE LIVEDATA IS NOT MODIFIED IF THE USER SET THE WORKOUT REFERENCE TO NULL
    val workoutReference = Transformations.switchMap(user) { user ->
        user?.workoutReferenceId?.let { workoutRepo.getWorkoutById(it) }
    }

    val routine = Transformations.switchMap(workoutReference) { workout ->
        workout?.routineId?.let { routineRepo.getRoutineById(it) }
    }

    val workoutSession: LiveData<Workout?> = Transformations.switchMap(user) { user ->
        if (user?.workoutSessionId != null) {
            workoutRepo.getWorkoutById(user.workoutSessionId!!)
        } else {
            MutableLiveData<Workout?>(null)
        }
    }.distinctUntilChanged()

    suspend fun deleteWorkoutSession() {
        workoutRepo.deleteWorkout(workoutSession.value!!)
    }

    suspend fun deleteWorkout(workoutId: Long) {
        workoutRepo.deleteWorkoutById(workoutId)
    }

    suspend fun finishCurrentWorkoutSession(updateToNext: Boolean = true) {
        workoutSession.value?.let { workoutSession ->
            // add date to the session
            workoutSession.finishDate = Date()
            workoutRepo.updateWorkout(workoutSession)

            // update user workoutReferenceId
            if (updateToNext) {
                updateToNextWorkout(workoutSession)
            }

            workoutRepo.updateUserWorkoutSession(null)
        }
    }

    // set user workout reference to the next one based on the routine
    private suspend fun updateToNextWorkout(workoutSession: Workout) {
        val routineId = workoutSession.routineId!!
        val workouts = workoutRepo.allReferenceWorkoutByRoutineId(routineId)

        // Cycle order
        var newWorkoutOrderId = 0
        if (workoutSession.order < workouts.size - 1) {
            newWorkoutOrderId = workoutSession.order + 1
        }

        workoutRepo.updateUserWorkoutReference(workouts[newWorkoutOrderId].id!!)
    }

    // Create a Workout that will be the session workout
    suspend fun continueRoutineWorkout() {
        val newWorkout = workoutRepo.newWorkoutSessionFromReference(workoutReference.value!!)
        workoutRepo.updateUserWorkoutSession(newWorkout.id)
    }

    suspend fun createWorkoutSession(workout: Workout) {
        workout.id = workoutRepo.insertWorkout(workout)
    }

    suspend fun updateUser(user: User) {
        workoutRepo.updateUser(user)
    }

}