package com.zeng.myworkout.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(
    tableName = "load",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutExercise::class,
            parentColumns = ["id"],
            childColumns = ["workoutExerciseId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE)
    ]
)
data class Load(
    @ColumnInfo
    var type: LoadType = LoadType.WEIGHT,

    @ColumnInfo
    var value: Float = 0F,

    // Reference rep for routine
    @ColumnInfo
    var reps: Int = 0,

    @ColumnInfo
    var order: Int = 0,

    @ColumnInfo(index = true)
    var workoutExerciseId: Long? = null,

    // Actual reps done in workouts
    @ColumnInfo
    var repsDone: Int = -1,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
)

enum class LoadType(val value: Int) {
    WEIGHT(0), // BY DEFAULT THE WEIGHT IS SET TO KG, IF LB DISPLAY BY DOING WEIGHT * 2.2
    BODYWEIGHT(1),
    DURATION(2);

    companion object {
        private val intMap = values().associateBy { it.value }
        private val stringMap = values().associateBy { it.name }
        fun fromInt(loadType: Int) = intMap[loadType]
        fun fromString(str: String) = stringMap[str]
    }
}

@Serializable
data class LoadSerialized(
    val type: LoadType = LoadType.WEIGHT,
    val value: Float = 0F,
    val reps: Int = 0
)