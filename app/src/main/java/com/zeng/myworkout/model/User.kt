package com.zeng.myworkout.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "user",
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["id"],
            childColumns = ["workoutReferenceId"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.CASCADE),
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["id"],
            childColumns = ["workoutSessionId"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.CASCADE)
    ]
)
data class User(
    // Id of the workout reference from the routine
    @ColumnInfo(index = true)
    var workoutReferenceId: Long? = null,

    @ColumnInfo
    var current: Boolean = false,

    // Id of current session
    @ColumnInfo(index = true)
    var workoutSessionId: Long? = null,

    @ColumnInfo
    var customSession: Boolean = false,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
)