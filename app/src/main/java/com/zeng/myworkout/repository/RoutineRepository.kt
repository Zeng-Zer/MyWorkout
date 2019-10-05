package com.zeng.myworkout.repository

import androidx.lifecycle.LiveData
import com.zeng.myworkout.database.RoutineDao
import com.zeng.myworkout.model.Routine

class RoutineRepository private constructor(private val routineDao: RoutineDao) {

    fun getAllRoutine(): LiveData<List<Routine>> = routineDao.getAllRoutine()
    fun getRoutineById(routineId: Long): LiveData<Routine> = routineDao.getRoutineById(routineId)
    suspend fun routineById(routineId: Long): Routine = routineDao.routineById(routineId)

    suspend fun insertRoutine(routine: Routine) = routineDao.insertRoutineReorder(routine)
    suspend fun update(routines: List<Routine>) = routineDao.update(routines)
    suspend fun upsert(routines: List<Routine>) = routineDao.upsert(routines)
    suspend fun deleteRoutine(routine: Routine) = routineDao.deleteRoutineReorder(routine)

    suspend fun getReferenceWorkoutCount(routineId: Long) = routineDao.getReferenceWorkoutCount(routineId)

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
