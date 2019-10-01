package com.zeng.myworkout.model

class Load {

    enum class LoadType {
        BODYWEIGHT,
        WEIGHT, // BY DEFAULT THE WEIGHT IS SET TO KG, IF LB DISPLAY BY DOING WEIGHT * 2.2
        TIME
    }

    lateinit var type: LoadType
    var value: Float = 0F
    var reps: Int = 0
}
