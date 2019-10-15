package com.zeng.myworkout.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.zeng.myworkout.R
import com.zeng.myworkout.database.AppDatabase
import com.zeng.myworkout.model.*
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list
import org.koin.core.KoinComponent
import org.koin.core.inject

class DatabaseWorker(
    val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val database: AppDatabase by inject()

    override suspend fun doWork(): Result = coroutineScope {
        try {
            populateDatabase()
            Result.success()
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }

    private suspend fun populateDatabase() {
        val json = Json(JsonConfiguration.Stable)
        val categories: List<Category> = json.parse(
            Category.serializer().list,
            context.resources.openRawResource(R.raw.categories).bufferedReader().use {it.readText()}
        )
        val exercises: List<Exercise> = json.parse(
            Exercise.serializer().list,
            context.resources.openRawResource(R.raw.exercises).bufferedReader().use {it.readText()}
        )
        val routines: List<RoutineSerialized> = json.parse(
            RoutineSerialized.serializer().list,
            context.resources.openRawResource(R.raw.routines).bufferedReader().use {it.readText()}
        )

        insertUser(User(null, true))
        insertCategories(categories)
        insertExercises(exercises)
        val exerciseIds = exercises.map { it.name to it.id }.toMap()
        insertRoutines(routines, categories, exerciseIds)
    }

    private suspend fun insertUser(user: User) {
        user.id = database.userDao().insert(user)
    }

    // TODO REAL CATEGORY IMPLEMENTATION
    private suspend fun insertCategories(categories: List<Category>) {
        database.categoryDao().insert(categories)
    }

    private suspend fun insertExercises(exercises: List<Exercise>) {
        val ids = database.exerciseDao().insert(exercises)
        exercises.forEachIndexed { i, item ->
            item.id = ids[i]
        }
    }

    // TODO OPTIMIZE THIS
    private suspend fun insertRoutines(routines: List<RoutineSerialized>, categories: List<Category>, exerciseIds: Map<String, Long?>) {
        routines.mapIndexed { i, routine ->

            // Routines + Workouts
            Routine(routine.name, routine.description, i) to routine.workouts
        }.forEach { (routine, workouts) ->
            routine.id = database.routineDao().insertRoutineReorder(routine)
            workouts.mapIndexed { i, workout ->

                // Workout + WorkoutExercises
                Workout(workout.name, workout.description, i, routine.id, workout.reference) to workout.exercises
            }.forEach { (workout, exercises) ->
                workout.id = database.workoutDao().insert(workout)
                exercises.mapIndexed { i, exercise ->

                    // WorkoutExercise + Loads
                    WorkoutExercise(i, workout.id, exerciseIds[exercise.name]) to exercise.loads
                }.forEach { (workoutExercise, loads) ->
                    workoutExercise.id = database.workoutExerciseDao().insert(workoutExercise)

                    // Load
                    val insertLoads = loads.mapIndexed { i, load ->
                        Load(load.type, load.value, load.reps, i, workoutExercise.id)
                    }
                    database.loadDao().insert(insertLoads)
                }
            }
        }
    }

    companion object {
        private val TAG = DatabaseWorker::class.java.simpleName
    }
}
