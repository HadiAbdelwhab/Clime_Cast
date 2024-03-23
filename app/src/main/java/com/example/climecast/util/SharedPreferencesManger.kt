package com.example.climecast.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.climecast.util.Constants.LANGUAGE_PREFERENCES
import com.example.climecast.util.Constants.TEMP_UNIT_PREFERENCES
import com.example.climecast.util.Constants.WIND_UNIT_PREFERENCES

class SharedPreferencesManger {


    companion object {
        private lateinit var sharedPreferences: SharedPreferences
        fun getSharedPreferencesManagerTempUnit(context: Context): String {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val temperatureUnit = sharedPreferences.getString(TEMP_UNIT_PREFERENCES, "Kelvin")
            return temperatureUnit.toString()

        }

        fun getSharedPreferencesManagerWindUnit(context: Context): String {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val temperatureUnit = sharedPreferences.getString(WIND_UNIT_PREFERENCES, "meter/second")
            return temperatureUnit.toString()

        }

        fun getSharedPreferencesManagerLanguage(context: Context): String {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return sharedPreferences.getString(LANGUAGE_PREFERENCES, "en").toString()
        }
    }

}