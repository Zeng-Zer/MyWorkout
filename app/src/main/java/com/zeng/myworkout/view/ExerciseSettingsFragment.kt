package com.zeng.myworkout.view

import android.os.Bundle
import android.text.InputType
import androidx.navigation.fragment.navArgs
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat

class ExerciseSettingsFragment : PreferenceFragmentCompat() {

    private val args by navArgs<ExerciseSettingsFragmentArgs>()
    private val exerciseSettingsId by lazy { args.exerciseSettingsId }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val context = preferenceManager.context
        val screen = preferenceManager.createPreferenceScreen(context)

        val restTime = EditTextPreference(context)
        restTime.key = "rest_$exerciseSettingsId"
        restTime.title = "Rest Time in sec"
        restTime.text = "90"
        restTime.summaryProvider = EditTextPreference.SimpleSummaryProvider.getInstance()
        restTime.setOnBindEditTextListener { it.inputType = InputType.TYPE_CLASS_NUMBER }

        val increment = EditTextPreference(context)
        increment.key = "increment_$exerciseSettingsId"
        increment.title = "Increment"
        increment.text = "5"
        increment.summaryProvider = EditTextPreference.SimpleSummaryProvider.getInstance()
        increment.setOnBindEditTextListener { it.inputType = InputType.TYPE_CLASS_NUMBER }

        val notes = EditTextPreference(context)
        notes.key = "note_$exerciseSettingsId"
        notes.title = "Notes"
        notes.text = "Write notes about how you felt during the exercise"
        notes.summaryProvider = EditTextPreference.SimpleSummaryProvider.getInstance()

        screen.addPreference(restTime)
        screen.addPreference(increment)
        screen.addPreference(notes)

        preferenceScreen = screen
    }

}
