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
    @Query("SELECT * FROM routine")
    abstract fun getAllRoutine(): LiveData<List<Routine>>

    @Delete
    abstract fun delete(routineSql: RoutineSql)

    @Insert
    abstract fun insertRoutineSql(routineSql: RoutineSql): Long

    @Transaction
    open suspend fun insert(routine: Routine): Long {
        val routineId = insertRoutineSql(routine)
        routine.id = routineId
        routine.workouts.forEach{
            it.routineId = routineId
        }
        return routineId
    }

}
