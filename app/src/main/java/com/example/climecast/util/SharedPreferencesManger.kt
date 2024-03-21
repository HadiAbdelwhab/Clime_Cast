package com.example.climecast.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.climecast.util.Constants.TEMP_UNIT_PER

class SharedPreferencesManger {


    companion object {
        private lateinit var sharedPreferences: SharedPreferences
        fun getSharedPreferencesManagerTempUnit(context: Context): String {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val temperatureUnit = sharedPreferences.getString(TEMP_UNIT_PER, "Kelvin")
            return temperatureUnit.toString()

        }
    }

}