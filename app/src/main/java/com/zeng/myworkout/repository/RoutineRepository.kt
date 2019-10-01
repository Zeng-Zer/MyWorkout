package com.zeng.myworkout.repository

import androidx.lifecycle.LiveData
import com.zeng.myworkout.database.RoutineDao
import com.zeng.myworkout.model.Routine

class RoutineRepository private constructor(private val routineDao: RoutineDao) {

    fun getAllRoutine(): LiveData<List<Routine>> = routineDao.getAllRoutine()
    fun getRoutineSqlById(routineId: Long): LiveData<Routine> = routineDao.getRoutineSqlById(routineId)

    suspend fun insertRoutine(routine: Routine): Long = routineDao.insertRoutineReorder(routine)
    suspend fun update(routines: List<Routine>) = routineDao.update(routines)
    suspend fun upsert(routines: List<Routine>) = routineDao.upsert(routines)
    suspend fun deleteRoutine(routine: Routine) = routineDao.deleteRoutineReorder(routine)

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: RoutineRepository? = null

        fun getInstance(routineDao: RoutineDao) =
            instance ?: synchronized(this) {
                instance
                    ?: RoutineRepository(routineDao).also { instance = it }
            }
    }
}
