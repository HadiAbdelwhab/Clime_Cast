package com.example.climecast.network

import com.example.climecast.util.Constants
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitHelper {
    fun getInstance(): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val original = chain.request()
                        val url = original.url().newBuilder()
                            .addQueryParameter("appid", Constants.API_KEY)
                            .build()
                        val request = original.newBuilder()
                            .url(url)
                            .build()
                        chain.proceed(request)
                    }
                    .build()
            )
            .build()
        return retrofit
    }

}