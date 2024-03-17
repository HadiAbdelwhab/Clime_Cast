package com.example.climecast.model

import com.google.gson.annotations.SerializedName

data class WeatherData(
    val cod: String,
    val message: Int,
    @SerializedName("cnt")
    val count: Int,
    val list: List<WeatherListItem>,
    val city: City
)

data class WeatherListItem(
    @SerializedName("dt")
    val dateTime: Long,
    val main: MainInfo,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    @SerializedName("pop")
    val precipitationProbability: Double,
    val rain: Rain?,
    val sys: Sys,
    @SerializedName("dt_txt")
    val dateTimeText: String
)

data class MainInfo(
    @SerializedName("temp")
    val temperature: Double,
    @SerializedName("feels_like")
    val feelsLikeTemperature: Double,
    @SerializedName("temp_min")
    val minTemperature: Double,
    @SerializedName("temp_max")
    val maxTemperature: Double,
    val pressure: Int,
    @SerializedName("sea_level")
    val seaLevel: Int,
    @SerializedName("grnd_level")
    val groundLevel: Int,
    val humidity: Int,
    @SerializedName("temp_kf")
    val temperatureKf: Double
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Clouds(
    val all: Int
)

data class Wind(
    val speed: Double,
    @SerializedName("deg")
    val degrees: Int,
    val gust: Double
)

data class Rain(

    @SerializedName("3h")
    val volumeForLast3Hours: Double
)

data class Sys(
    @SerializedName("pod")
    val partOfDay: String
)

data class City(
    val id: Int,
    val name: String,
    @SerializedName("cood")
    val coordinates: Coord,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)

data class Coord(
    val lat: Double,
    val lon: Double
)
