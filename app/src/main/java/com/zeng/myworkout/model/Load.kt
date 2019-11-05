package com.zeng.myworkout.model

import kotlinx.serialization.Serializable

data class Load(
    var type: LoadType = LoadType.WEIGHT,

    var value: Float = 0F,

    // Reference rep for routine
    var reps: Int = 0,

    // Actual reps done in workouts
    var repsDone: Int = -1
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