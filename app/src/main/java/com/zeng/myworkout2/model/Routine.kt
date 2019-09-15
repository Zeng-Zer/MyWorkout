package com.zeng.myworkout2.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable

class Routine(
    @Relation(parentColumn = "id", entityColumn = "routineId", entity = WorkoutSql::class)
    var workouts: List<Workout> = listOf(),
    name: String = "",
    description: String = "",
    id: Long? = null
) : RoutineSql(name, description, id)

@Entity(tableName = "routine")
open class RoutineSql(
    @ColumnInfo
    var name: String = "",

    @ColumnInfo
    var description: String = "",

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

) : Serializable