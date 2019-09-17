package com.zeng.myworkout2.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "exercise")
data class Exercise(
    @ColumnInfo
    var name: String = "",

    @ColumnInfo
    var category: String = "",

    @ColumnInfo
    var deleted: Boolean = false,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

) : Serializable
