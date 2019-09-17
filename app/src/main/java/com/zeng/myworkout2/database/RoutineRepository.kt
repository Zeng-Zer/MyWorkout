package com.zeng.myworkout2.database

import androidx.lifecycle.LiveData
import com.zeng.myworkout2.model.Routine

class RoutineRepository private constructor(private val routineDao: RoutineDao) {

    fun getRoutines(): LiveData<List<Routine>> = routineDao.getAllRoutine()
    fun getRoutineById(routineId: Long): LiveData<Routine> = routineDao.getRoutineById(routineId)
    suspend fun addRoutine(routine: Routine): Long = routineDao.insert(routine)
    suspend fun deleteRoutine(routine: Routine) = routineDao.delete(routine)

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: RoutineRepository? = null

        fun getInstance(routineDao: RoutineDao) =
            instance ?: synchronized(this) {
                instance ?: RoutineRepository(routineDao).also { instance = it }
            }
    }
}
