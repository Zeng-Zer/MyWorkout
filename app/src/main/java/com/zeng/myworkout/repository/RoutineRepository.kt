package com.zeng.myworkout.repository

import androidx.lifecycle.LiveData
import com.zeng.myworkout.database.RoutineDao
import com.zeng.myworkout.model.Routine
import com.zeng.myworkout.model.RoutineWithWorkouts

class RoutineRepository(private val routineDao: RoutineDao) {

    fun getAllRoutineWithWorkouts(): LiveData<List<RoutineWithWorkouts>> = routineDao.getAllRoutineWithReferenceWorkouts()
    fun getRoutineById(routineId: Long): LiveData<Routine> = routineDao.getRoutineById(routineId)

    suspend fun insertRoutine(routine: Routine) = routineDao.insertRoutineReorder(routine)
    suspend fun update(routines: List<Routine>) = routineDao.update(routines)
    suspend fun deleteRoutine(routine: Routine) = routineDao.deleteRoutineReorder(routine)
}
