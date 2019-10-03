package com.zeng.myworkout.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
open class User(
    @ColumnInfo
    var workoutId: Long? = null,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
)
