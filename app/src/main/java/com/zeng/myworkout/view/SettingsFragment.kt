package com.zeng.myworkout.view

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.zeng.myworkout.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}
