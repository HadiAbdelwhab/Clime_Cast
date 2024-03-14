package com.example.climecast.network



private const val TAG = "WeatherRemoteDataSourceImpl"

class WeatherRemoteDataSourceImpl : WeatherRemoteDataSource {


    private val weatherService: WeatherService by lazy {
        RetrofitHelper.getInstance().create(WeatherService::class.java)
    }

    companion object {
        @Volatile
        private var instance: WeatherRemoteDataSourceImpl? = null

        fun getInstance(): WeatherRemoteDataSourceImpl {
            return instance ?: synchronized(this) {
                instance ?: WeatherRemoteDataSourceImpl().also { instance = it }
            }
        }
    }
}