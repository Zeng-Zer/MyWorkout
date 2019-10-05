package com.zeng.myworkout.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.zeng.myworkout.model.Load

@Dao
abstract class LoadDao : BaseDao<Load>() {

    @Transaction
    @Query("SELECT * FROM load WHERE workoutExerciseId = :workoutExerciseId ORDER BY [order] ASC")
    protected abstract fun getAllLoadByIdNotDistinct(workoutExerciseId: Long): LiveData<List<Load>>

    fun getAllLoadById(workoutExerciseId: Long): LiveData<List<Load>> = getAllLoadByIdNotDistinct(workoutExerciseId).distinctUntilChanged()

    @Transaction
    @Query("SELECT * FROM load WHERE workoutExerciseId = :workoutExerciseId ORDER BY [order] ASC")
    abstract suspend fun allLoadById(workoutExerciseId: Long): List<Load>
}
