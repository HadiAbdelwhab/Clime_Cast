package com.example.climecast.model

data class WeatherBytTimeResponse(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int,
    val data: List<WeatherTimeData>
)

data class WeatherTimeData(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Double,
    val uvi: Double,
    val clouds: Int,
    val visibility: Int,
    val wind_speed: Double,
    val wind_deg: Int,
    val weather: List<WeatherInfo>
)

data class WeatherInfo(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)
