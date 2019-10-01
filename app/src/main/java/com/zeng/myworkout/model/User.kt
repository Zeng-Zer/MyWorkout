package com.zeng.myworkout.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
open class User(
    var workoutId: Long? = null,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
)
