package com.example.climecast.util

import kotlin.math.min

class WeatherUtils {
    companion object {
        fun kelvinToCelsius(kelvin: Double): String {
            val result = kelvin - 273.15
            val roundedResult = String.format("%.2f", result)
            return roundedResult.substring(0, minOf(roundedResult.length, 2))
        }

        fun kelvinToFahrenheit(kelvin: Double): String {
            val celsius = kelvin - 273.15
            val result = (celsius * 9 / 5) + 32
            val roundedResult = String.format("%.2f", result)
            return roundedResult.substring(0, minOf(roundedResult.length, 2))
        }

        fun meterPerSecondToMilesPerHour(mps: Double): Double {
            return mps * 2.23694 // 1 meter per second is approximately equal to 2.23694 miles per hour
        }
    }
}
