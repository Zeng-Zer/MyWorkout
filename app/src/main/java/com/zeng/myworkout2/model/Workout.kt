package com.zeng.myworkout2.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable

@Entity(tableName = "workout")
open class WorkoutSql(
    @ColumnInfo
    var name: String = "",

    @ColumnInfo
    var description: String = "",

    @ColumnInfo
    var routineId: Long? = null,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

) : Serializable

class Workout(
    @Relation(parentColumn = "id", entityColumn = "workoutId", entity = WorkoutExerciseSql::class)
    var exercises: List<WorkoutExercise> = listOf(),
    name: String = "",
    description: String = "",
    routineId: Long? = null,
    id: Long? = null
) : WorkoutSql(name, description, routineId, id)