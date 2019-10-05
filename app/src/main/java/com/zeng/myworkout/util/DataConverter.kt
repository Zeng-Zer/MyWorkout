package com.zeng.myworkout.util

import androidx.room.TypeConverter
import com.zeng.myworkout.model.LoadType
import java.util.*

class DataConverter {
    @TypeConverter
    fun fromType(value: LoadType): Int {
        return value.value
    }

    @TypeConverter
    fun toType(value: Int): LoadType {
        return LoadType.fromInt(value)!!
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(value: Date?): Long? {
        return value?.time
    }
}