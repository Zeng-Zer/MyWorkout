package com.zeng.myworkout2.worker


import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.zeng.myworkout2.database.AppDatabase
import com.zeng.myworkout2.model.*
import kotlinx.coroutines.coroutineScope

class DatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {
        try {
            val database = AppDatabase.getInstance(applicationContext)
            populateDatabase(database)
            Result.success()
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }

    private suspend fun populateDatabase(database: AppDatabase) {
        val userDao = database.userDao()
        val routineDao = database.routineDao()
        val workoutDao = database.workoutDao()
        val exerciseDao = database.exerciseDao()

        val squatExercise = Exercise("Squat", "Legs")
        val benchExercise = Exercise("Bench Press", "Chest")
        val deadliftExercise = Exercise("Deadlift", "Legs")

        squatExercise.id = exerciseDao.insert(squatExercise)
        benchExercise.id = exerciseDao.insert(benchExercise)
        deadliftExercise.id = exerciseDao.insert(deadliftExercise)

        val squat = WorkoutExercise(
            squatExercise,
            5,
            5,
            120f,
            0
        )

        val bench = WorkoutExercise(
            benchExercise,
            10,
            10,
            60f,
            1
        )

        val deadlift = WorkoutExercise(
            deadliftExercise,
            1,
            5,
            200f,
            2
        )

        val workout = Workout(
            listOf(squat, bench, deadlift),
            "Fullbody",
            "test",
            1
        )

        val workout2 = Workout(
            listOf(squat),
            "Fullbody2",
            "test",
            1
        )

        val routine = Routine(
            listOf(workout, workout2),
            "Fullbody - Test",
            "2x / week",
            0
        )
        val routine2 = Routine(emptyList(), "Routine 2 - Test", "testing ordering", 0)
        routineDao.insertRoutine(routine)
        routineDao.insertRoutine(routine2)

        workout.id = 8
        workoutDao.insert(exerciseDao, workout)
        workout2.id = workoutDao.insert(exerciseDao, workout2)

        val user = User(workout, 0)
        userDao.insert(user)
    }

    companion object {
        private val TAG = DatabaseWorker::class.java.simpleName
    }
}
