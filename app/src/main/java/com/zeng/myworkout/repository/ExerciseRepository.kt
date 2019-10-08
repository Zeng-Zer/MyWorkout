package com.zeng.myworkout.repository

import com.zeng.myworkout.database.CategoryDao
import com.zeng.myworkout.database.ExerciseDao

class ExerciseRepository(private val exerciseDao: ExerciseDao, private val categoryDao: CategoryDao) {

    fun getAllExercise() = exerciseDao.getAllExercise()

}
