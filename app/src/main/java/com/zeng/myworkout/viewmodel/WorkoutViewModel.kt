package com.zeng.myworkout.viewmodel

import androidx.lifecycle.*
import com.zeng.myworkout.model.Routine
import com.zeng.myworkout.model.Workout
import com.zeng.myworkout.model.WorkoutExercise
import com.zeng.myworkout.model.WorkoutExerciseDetail
import com.zeng.myworkout.repository.RoutineRepository
import com.zeng.myworkout.repository.WorkoutRepository
import kotlinx.coroutines.launch

class WorkoutViewModel(private val workoutRepo: WorkoutRepository, private val routineRepo: RoutineRepository): ViewModel() {

    val workoutId: MutableLiveData<Long?> = MutableLiveData(null)
    val exercises: LiveData<List<WorkoutExerciseDetail>> = workoutId.switchMap { id ->
        if (id != null) {
            workoutRepo.getAllWorkoutExerciseById(id)
        } else {
            MutableLiveData()
        }
    }

    suspend fun workoutById(workoutId: Long) = workoutRepo.workoutById(workoutId)
    suspend fun referenceWorkoutsByRoutineId(routineId: Long) = workoutRepo.allReferenceWorkoutByRoutineId(routineId)

    fun deleteWorkoutExercise(exercise: WorkoutExercise) {
        viewModelScope.launch {
            workoutRepo.deleteWorkoutExercise(exercise)
        }
    }

    fun updateWorkoutExercise(exercise: WorkoutExercise) {
        viewModelScope.launch {
            workoutRepo.updateWorkoutExercise(exercise)
        }
    }

    fun updateWorkoutExercise(list: List<WorkoutExercise>) {
        viewModelScope.launch {
            workoutRepo.updateWorkoutExercise(list)
        }
    }

    fun insertWorkoutExercises(exercises: List<WorkoutExercise>) {
        viewModelScope.launch {
            workoutRepo.insertWorkoutExercise(exercises)
        }
    }

    suspend fun insertRoutine(routine: Routine) {
        routine.id = routineRepo.insertRoutine(routine)
    }

    suspend fun insertWorkout(workout: Workout) {
        workout.id = workoutRepo.insertWorkout(workout)
    }

}