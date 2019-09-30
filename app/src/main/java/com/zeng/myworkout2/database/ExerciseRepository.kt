package com.zeng.myworkout2.database

class ExerciseRepository private constructor(
    private val exerciseDao: ExerciseDao,
    private val categoryDao: CategoryDao
) {

    fun getAllExercise() = exerciseDao.getAllExercise()

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: ExerciseRepository? = null

        fun getInstance(exerciseDao: ExerciseDao, categoryDao: CategoryDao) =
            instance ?: synchronized(this) {
                instance ?: ExerciseRepository(exerciseDao, categoryDao).also { instance = it }
            }
    }
}
