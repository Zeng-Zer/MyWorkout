package com.zeng.myworkout.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "workout")
open class Workout(
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
