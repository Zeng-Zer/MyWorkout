package com.zeng.myworkout.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "detail")
data class Exercise(
    @ColumnInfo
    var name: String = "",

    // TODO FOREIGN KEY UPDATE
    @ColumnInfo
    var category: String = "",

    @ColumnInfo
    var deleted: Boolean = false,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

) : Serializable
