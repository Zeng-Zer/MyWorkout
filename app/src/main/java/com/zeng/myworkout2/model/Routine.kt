package com.zeng.myworkout2.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable

class Routine(
    @Relation(parentColumn = "id", entityColumn = "routineId", entity = WorkoutSql::class)
    var workouts: List<WorkoutSql> = listOf(),
    name: String = "",
    description: String = "",
    order: Int = 0,
    id: Long? = null
) : RoutineSql(name, description, order, id)

@Entity(tableName = "routine")
open class RoutineSql(
    @ColumnInfo
    var name: String = "",

    @ColumnInfo
    var description: String = "",

    @ColumnInfo
    var order: Int = 0,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

) : Serializable