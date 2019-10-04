package com.zeng.myworkout.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.zeng.myworkout.model.Routine

@Dao
abstract class RoutineDao : BaseDao<Routine>() {

    @Transaction
    @Query("SELECT * FROM routine WHERE id =:id")
    abstract fun getRoutineById(id: Long): LiveData<Routine>

    @Transaction
    @Query("SELECT * FROM routine ORDER BY [order] ASC")
    abstract fun getAllRoutine(): LiveData<List<Routine>>

    @Transaction
    @Query("SELECT * FROM routine ORDER BY [order] ASC")
    abstract fun allRoutine(): List<Routine>

    @Transaction
    open suspend fun insertRoutineReorder(routine: Routine): Long {
        val routines = allRoutine()
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
    open suspend fun deleteRoutineReorder(routine: Routine) {
        val routines = allRoutine()
        val updateList = routines
            .filter { r -> r.order > routine.order }
            .map { r -> r.order -= 1; r }

        // TODO DELETE REFERENCE WORKOUTS
        delete(routine)
        update(updateList)
    }
}
