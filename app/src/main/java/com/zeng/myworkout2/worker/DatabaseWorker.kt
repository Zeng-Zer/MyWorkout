package com.zeng.myworkout2.worker


import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.zeng.myworkout2.database.AppDatabase
import com.zeng.myworkout2.model.Exercise
import com.zeng.myworkout2.model.User
import com.zeng.myworkout2.model.Workout
import com.zeng.myworkout2.model.WorkoutExercise
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
        val exerciseDao = database.exerciseDao()
        val workoutDao = database.workoutDao()
        val squat = WorkoutExercise(
            Exercise("Squat", "Legs"),
            listOf(5, 5, 5, 5, 5),
            120f,
            0
        )


        val deadlift = WorkoutExercise(
            Exercise("Deadlift", "Legs"),
            listOf(5),
            200f,
            1
        )

        val bench = WorkoutExercise(
            Exercise("Bench Press", "Chest"),
            listOf(10, 10, 10, 10, 10, 10, 10, 10, 10, 10),
            60f,
            2
        )

        val workout = Workout(
            listOf(squat, deadlift, bench),
            "Fullbody",
            "test"
        )

        val workout2 = Workout(
            listOf(squat, deadlift, bench),
            "Fullbody2",
            "test"
        )
        workout.id = workoutDao.insert(exerciseDao, workout)

        val user = User(workout, 0)
        database.userDao().insert(user)

        workout2.id = 8
        workoutDao.insert(exerciseDao, workout2)
    }

    companion object {
        private val TAG = DatabaseWorker::class.java.simpleName
    }
}
