package com.example.climecast.ui.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.example.climecast.R
import com.example.climecast.ui.MainActivity
import java.util.*

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val languagePreference: ListPreference? = findPreference("language")
        languagePreference?.setOnPreferenceChangeListener { _, newValue ->
            val languageCode = newValue as String
            setAppLocale(requireContext(), languageCode)

            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)

            true
        }

        val switchPreference = findPreference<SwitchPreferenceCompat>("mode")
        switchPreference?.setOnPreferenceChangeListener { _, newValue ->
            val isEnabled = newValue as Boolean
            //activity.setTheme(R.s)

            true
        }


        val switchPreferenceNotification = findPreference<SwitchPreferenceCompat>("notification")
        switchPreferenceNotification?.setOnPreferenceChangeListener { _, newValue ->
            val isEnabled = newValue as Boolean
            PreferenceManager.getDefaultSharedPreferences(requireActivity()).edit()
                .putBoolean("notification", isEnabled).apply()

            true
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(
        sharedPreferences: SharedPreferences?,
        key: String?
    ) {
    }

    private fun setAppLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}
