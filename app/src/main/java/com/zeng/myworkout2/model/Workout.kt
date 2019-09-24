package com.zeng.myworkout2.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable

// TODO REMOVE THESE CLASSES KEEP ONLY SQL
class Workout(
    @Relation(parentColumn = "id", entityColumn = "workoutId", entity = WorkoutExerciseSql::class)
    var exercises: List<WorkoutExercise> = listOf(),
    name: String = "",
    description: String = "",
    order: Int = 0,
    routineId: Long? = null,
    id: Long? = null
) : WorkoutSql(name, description, order, routineId, id)

@Entity(tableName = "workout")
open class WorkoutSql(
    @ColumnInfo
    var name: String = "",

    @ColumnInfo
    var description: String = "",

    @ColumnInfo
    var order: Int = 0,
    // TODO ADD BOOLEAN FOR REFERENCE WORKOUT OR ACTUAL SESSION

    // TODO SETUP FOREIGN KEY - DELETE CASCADE ?
    @ColumnInfo
    var routineId: Long? = null,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

) : Serializable
