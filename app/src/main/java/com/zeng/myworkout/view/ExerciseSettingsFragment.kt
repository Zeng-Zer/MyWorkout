package com.zeng.myworkout.view

import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat

class ExerciseSettingsFragment : PreferenceFragmentCompat() {


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val context = preferenceManager.context
        val screen = preferenceManager.createPreferenceScreen(context)

        val restTime = EditTextPreference(context)
        restTime.key = "rest"
        restTime.title = "Rest Time"
        restTime.summary = "How long should I rest between sets"
        restTime.text = "30sec"

        val increment = EditTextPreference(context)
        increment.key = "increment"
        increment.title = "Increment"
        increment.summary = "How heavy should the increment be after a successful workout"
        increment.text = "5"

        screen.addPreference(restTime)
        screen.addPreference(increment)

        preferenceScreen = screen
    }

}
