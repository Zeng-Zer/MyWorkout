package com.zeng.myworkout.util

import androidx.lifecycle.MutableLiveData
import java.text.DecimalFormat
import kotlin.math.roundToInt

operator fun <T> MutableLiveData<List<T>>.plusAssign(newValue: T) {
    value =
        if (value == null) {
            listOf(newValue)
        } else {
            value!! + newValue
        }
}

operator fun <T> MutableLiveData<List<T>>.plusAssign(newValues: List<T>) {
    value =
        if (value == null) {
            newValues
        } else {
            value!! + newValues
        }
}

operator fun <T> MutableLiveData<List<T>>.minusAssign(newValue: T) {
    value =
        if (value == null) {
            null
        } else {
            value!! - newValue
        }
}

operator fun <T> MutableLiveData<List<T>>.minusAssign(newValues: List<T>) {
    value =
        if (value == null) {
            null
        } else {
            value!! - newValues
        }
}

fun Float.weightToString(): String {
    // Imperial
    if (SettingsSingleton.isImperial()) {
        return DecimalFormat("#.##").format(this * SettingsSingleton.imperialCoef) + "lbs"
    }

    // Metric
    return if (this.roundToInt().toFloat() == this) {
        this.toInt().toString() + "kg"
    } else {
        DecimalFormat("#.##").format(this) + "kg"
    }
}
