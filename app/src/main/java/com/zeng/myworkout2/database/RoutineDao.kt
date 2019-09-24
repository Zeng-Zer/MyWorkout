package com.zeng.myworkout2.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zeng.myworkout2.model.Routine
import com.zeng.myworkout2.model.RoutineSql

@Dao
abstract class RoutineDao {

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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertRoutineSql(routine: RoutineSql): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertAllRoutineSql(routines: List<RoutineSql>): List<Long>

    @Transaction
    open suspend fun insert(routine: Routine): Long {
        val routineId = insertRoutineSql(routine)
        routine.id = routineId
        routine.workouts.forEach{
            it.routineId = routineId
        }
        return routineId
    }

    @Update
    abstract suspend fun updateAllRoutineSql(routines: List<RoutineSql>)

    @Transaction
    open suspend fun upsertAllRoutineSql(routines: List<RoutineSql>) {
        val updateList = insertAllRoutineSql(routines).zip(routines)
            .filter { (id, _) -> id == -1L }
            .map { (_, routine) -> routine }

        updateAllRoutineSql(updateList)
    }

    @Delete
    abstract suspend fun delete(routineSql: RoutineSql)
}
