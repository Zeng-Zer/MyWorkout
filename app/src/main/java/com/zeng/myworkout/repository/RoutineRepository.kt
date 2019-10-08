package com.zeng.myworkout.repository

import androidx.lifecycle.LiveData
import com.zeng.myworkout.database.RoutineDao
import com.zeng.myworkout.model.Routine

class RoutineRepository(private val routineDao: RoutineDao) {

    fun getAllRoutine(): LiveData<List<Routine>> = routineDao.getAllRoutine()
    fun getRoutineById(routineId: Long): LiveData<Routine> = routineDao.getRoutineById(routineId)

    suspend fun insertRoutine(routine: Routine) = routineDao.insertRoutineReorder(routine)
    suspend fun update(routines: List<Routine>) = routineDao.update(routines)
    suspend fun deleteRoutine(routine: Routine) = routineDao.deleteRoutineReorder(routine)
}
