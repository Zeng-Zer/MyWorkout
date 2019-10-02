package com.zeng.myworkout.model

enum class LoadType {
    BODYWEIGHT,
    WEIGHT, // BY DEFAULT THE WEIGHT IS SET TO KG, IF LB DISPLAY BY DOING WEIGHT * 2.2
    TIME
}

data class Load(
    var type: LoadType = LoadType.WEIGHT,
    var value: Float = 0F,
    var reps: Int = 0,
    var order: Int = 0
)
