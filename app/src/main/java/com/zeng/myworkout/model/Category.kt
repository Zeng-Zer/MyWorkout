package com.zeng.myworkout.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "category")
data class Category(
    @PrimaryKey(autoGenerate = false)
    val id: String
)
