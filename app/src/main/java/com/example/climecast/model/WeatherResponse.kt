package com.example.climecast.model

import com.google.gson.annotations.SerializedName

import java.io.Serializable

data class WeatherResponse(
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("timezone_offset") val timezoneOffset: Int,
    @SerializedName("current") val current: CurrentWeather,
    @SerializedName("minutely") val minutely: List<MinutelyData>,
    @SerializedName("hourly") val hourly: List<HourlyData>,
    @SerializedName("daily") val daily: List<DailyData>,
    @SerializedName("alerts") val alerts: List<Alert>
)

data class CurrentWeather(
    @SerializedName("dt") val timestamp: Long,
    @SerializedName("sunrise") val sunrise: Long,
    @SerializedName("sunset") val sunset: Long,
    @SerializedName("temp") val temperature: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("dew_point") val dewPoint: Double,
    @SerializedName("uvi") val uvIndex: Double,
    @SerializedName("clouds") val clouds: Int,
    @SerializedName("visibility") val visibility: Int,
    @SerializedName("wind_speed") val windSpeed: Double,
    @SerializedName("wind_deg") val windDegree: Int,
    @SerializedName("wind_gust") val windGust: Double,
    @SerializedName("weather") val weather: List<WeatherCondition>
)

data class MinutelyData(
    @SerializedName("dt") val timestamp: Long,
    @SerializedName("precipitation") val precipitation: Int
)

data class HourlyData(
    @SerializedName("dt") val timestamp: Long,
    @SerializedName("temp") val temperature: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("dew_point") val dewPoint: Double,
    @SerializedName("uvi") val uvIndex: Double,
    @SerializedName("clouds") val clouds: Int,
    @SerializedName("visibility") val visibility: Int,
    @SerializedName("wind_speed") val windSpeed: Double,
    @SerializedName("wind_deg") val windDegree: Int,
    @SerializedName("wind_gust") val windGust: Double,
    @SerializedName("weather") val weather: List<WeatherCondition>,
    @SerializedName("pop") val pop: Double
)

data class DailyData(
    @SerializedName("dt") val timestamp: Long,
    @SerializedName("sunrise") val sunrise: Long,
    @SerializedName("sunset") val sunset: Long,
    @SerializedName("moonrise") val moonrise: Long,
    @SerializedName("moonset") val moonset: Long,
    @SerializedName("moon_phase") val moonPhase: Double,
    @SerializedName("summary") val summary: String,
    @SerializedName("temp") val temperature: TemperatureData,
    @SerializedName("feels_like") val feelsLike: FeelsLikeData,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("dew_point") val dewPoint: Double,
    @SerializedName("wind_speed") val windSpeed: Double,
    @SerializedName("wind_deg") val windDegree: Int,
    @SerializedName("wind_gust") val windGust: Double,
    @SerializedName("weather") val weather: List<WeatherCondition>,
    @SerializedName("clouds") val clouds: Int,
    @SerializedName("pop") val pop: Double,
    @SerializedName("rain") val rain: Double?,
    @SerializedName("uvi") val uvIndex: Double
)

data class TemperatureData(
    @SerializedName("day") val day: Double,
    @SerializedName("min") val min: Double,
    @SerializedName("max") val max: Double,
    @SerializedName("night") val night: Double,
    @SerializedName("eve") val evening: Double,
    @SerializedName("morn") val morning: Double
)

data class FeelsLikeData(
    @SerializedName("day") val day: Double,
    @SerializedName("night") val night: Double,
    @SerializedName("eve") val evening: Double,
    @SerializedName("morn") val morning: Double
)

data class WeatherCondition(
    @SerializedName("id") val id: Int,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

data class Alert(
    @SerializedName("sender_name") val senderName: String,
    @SerializedName("event") val event: String,
    @SerializedName("start") val start: Long,
    @SerializedName("end") val end: Long,
    @SerializedName("description") val description: String,
    @SerializedName("tags") val tags: List<String>
)

data class LatLong(
    var lat: Double = 0.0,
    var long: Double = 0.0
) : Serializable