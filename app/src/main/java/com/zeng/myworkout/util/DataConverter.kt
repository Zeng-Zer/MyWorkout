package com.zeng.myworkout.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zeng.myworkout.model.Load
import com.zeng.myworkout.model.LoadType

class DataConverter {
    @TypeConverter
    fun fromSets(value: List<Load>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Load>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toSets(value: String): List<Load> {
        val gson = Gson()
        val type = object : TypeToken<List<Load>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromType(value: LoadType): Int {
        return value.value
    }

    @TypeConverter
    fun toType(value: Int): LoadType {
        return LoadType.fromInt(value)!!
    }
}