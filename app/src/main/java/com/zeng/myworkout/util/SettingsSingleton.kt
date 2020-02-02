package com.zeng.myworkout.util

import android.content.Context
import androidx.preference.PreferenceManager
import org.koin.core.KoinComponent
import org.koin.core.inject

object SettingsSingleton : KoinComponent {
    private val context by inject<Context>()
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    val imperialCoef = 2.205f
    fun isImperial(): Boolean = preferences.getBoolean("imperial", false)
}