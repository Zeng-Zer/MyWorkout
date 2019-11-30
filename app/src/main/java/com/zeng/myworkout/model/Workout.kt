package com.zeng.myworkout.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.*

@Entity(tableName = "workout")
data class Workout(
    @ColumnInfo
    var name: String = "",

    @ColumnInfo
    var description: String = "",

    @ColumnInfo
    var order: Int = 0,

    @ColumnInfo
    var routineId: Long? = null,

    @ColumnInfo
    var reference: Boolean = false,

    @ColumnInfo
    var startDate: Date? = null,

    @ColumnInfo
    var finishDate: Date? = null,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
)

@Serializable
data class WorkoutSerialized(
    val name: String = "",
    val description: String = "",
    val reference: Boolean = false,
    val exercises: List<WorkoutExerciseSerialized> = emptyList()
)

data class WorkoutWithExercises(
    val workout: Workout,
    val exercises: List<WorkoutExerciseDetail>,
    val routine: String?
)