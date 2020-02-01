package com.zeng.myworkout.util

import android.content.Context
import androidx.preference.PreferenceManager
import org.koin.core.KoinComponent
import org.koin.core.inject

object SettingsSingleton : KoinComponent {
    private val context by inject<Context>()
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    fun isImperial(): Boolean = preferences.getBoolean("imperial", false)
}