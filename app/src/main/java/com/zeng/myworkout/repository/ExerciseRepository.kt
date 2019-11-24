package com.zeng.myworkout.repository

import com.zeng.myworkout.database.CategoryDao
import com.zeng.myworkout.database.ExerciseDao
import com.zeng.myworkout.model.Exercise

class ExerciseRepository(private val exerciseDao: ExerciseDao, private val categoryDao: CategoryDao) {

    fun getAllExercise() = exerciseDao.getAllExercise()
    suspend fun addExercise(exercise: Exercise) = exerciseDao.insert(exercise)

    suspend fun getAllCategory() = categoryDao.getAllCategory()
}
