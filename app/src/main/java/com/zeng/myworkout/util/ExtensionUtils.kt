package com.zeng.myworkout.util

import androidx.lifecycle.MutableLiveData

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
