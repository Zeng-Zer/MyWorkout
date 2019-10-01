package com.zeng.myworkout.worker


import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.zeng.myworkout.database.AppDatabase
import com.zeng.myworkout.database.Category
import com.zeng.myworkout.model.*
import kotlinx.coroutines.coroutineScope

class DatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private lateinit var database: AppDatabase

    override suspend fun doWork(): Result = coroutineScope {
        try {
            database = AppDatabase.getInstance(applicationContext)
            populateDatabase()
            Result.success()
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }

    private suspend fun insertRoutine(routine: Routine) {
        routine.id = database.routineDao().insert(routine)
    }

    private suspend fun insertWorkout(workout: Workout) {
        workout.id = database.workoutDao().insert(workout)
    }

    private suspend fun insertCategory(category: Category) {
        database.categoryDao().insert(category)
    }

    private suspend fun insertExercise(exercise: Exercise) {
        exercise.id = database.exerciseDao().insert(exercise)
    }

    private suspend fun insertWorkoutExercise(workoutExercise: WorkoutExercise) {
        workoutExercise.id = database.workoutExerciseDao().insert(workoutExercise)
    }

    private suspend fun insertUser(user: User) {
        user.id = database.userDao().insert(user)
    }

    private suspend fun populateDatabase() {
        // Category
        val legs = Category("Legs").also { insertCategory(it) }
        val chest = Category("Chest").also { insertCategory(it) }

        // Exercise
        val squatExercise = Exercise("Squat", legs.id).also { insertExercise(it) }
        val benchExercise = Exercise("Bench Press", chest.id).also { insertExercise(it) }
        val deadliftExercise = Exercise("Deadlift", legs.id).also { insertExercise(it) }

        val routine = Routine("Fullbody - Test", "2x / week", 0).also { insertRoutine(it) }

        val workout = Workout("Workout A", "test", 0, routine.id!!).also { insertWorkout(it) }

        // Add squat to workout
        WorkoutExercise(5, 5, 120f, 0, workout.id!!, squatExercise.id!!).also { insertWorkoutExercise(it) }
        // Add bench to workout
        WorkoutExercise(10, 10, 60f, 1, workout.id!!, benchExercise.id!!).also { insertWorkoutExercise(it) }
        // Add deadlift to workout
        WorkoutExercise(1, 5, 200f, 2, workout.id!!, deadliftExercise.id!!).also { insertWorkoutExercise(it) }

        val workout2 = Workout("Workout B", "test", 1, routine.id!!).also { insertWorkout(it) }

        // Add squat to workout2
        WorkoutExercise(5, 5, 120f, 0, workout2.id!!, squatExercise.id!!).also { insertWorkoutExercise(it) }

        User(workout.id, 0).also { insertUser(it) }

        // TESTING ROUTING TODO REMOVE
        val routine2 = Routine("Routine 2 - Test", "testing ordering", 1).also { insertRoutine(it) }
    }

    companion object {
        private val TAG = DatabaseWorker::class.java.simpleName
    }
}
