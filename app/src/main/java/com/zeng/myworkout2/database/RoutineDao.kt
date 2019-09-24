package com.zeng.myworkout2.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.zeng.myworkout2.model.Routine
import com.zeng.myworkout2.model.RoutineSql

@Dao
abstract class RoutineDao : BaseDao<RoutineSql>() {

    @Transaction
    @Query("SELECT * FROM routine WHERE id =:id")
    abstract fun getRoutineById(id: Long): LiveData<Routine>

    @Transaction
    @Query("SELECT * FROM routine WHERE id =:id")
    abstract fun getRoutineSqlById(id: Long): LiveData<RoutineSql>

    @Transaction
    @Query("SELECT * FROM routine ORDER BY [order] ASC")
    abstract fun getAllRoutine(): LiveData<List<Routine>>

    @Transaction
    @Query("SELECT * FROM routine ORDER BY [order] ASC")
    abstract fun getAllRoutineSql(): LiveData<List<RoutineSql>>

    @Transaction
    @Query("SELECT * FROM routine ORDER BY [order] ASC")
    abstract fun allRoutineSql(): List<RoutineSql>

    @Transaction
    open suspend fun insertRoutine(routine: Routine): Long {
        val routineId = insert(routine)
        routine.id = routineId
        routine.workouts.forEach{
            it.routineId = routineId
        }
        return routineId
    }


    @Transaction
    open suspend fun insertRoutineSql(routine: RoutineSql): Long {
        val routines = allRoutineSql()
        val updateList = routines
            .filter { r -> r.order >= routine.order }
            .map { r -> r.order += 1; r }

        update(updateList)
        return insert(routine)
    }

    /**
     * Delete Routine and update other routines' position
     */
    @Transaction
    open suspend fun deleteRoutineSql(routine: RoutineSql) {
        val routines = allRoutineSql()
        val updateList = routines
            .filter { r -> r.order > routine.order }
            .map { r -> r.order -= 1; r }

        delete(routine)
        update(updateList)
    }
}
