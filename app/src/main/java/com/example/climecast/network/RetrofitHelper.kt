package com.example.climecast.network

import com.example.climecast.util.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    fun getInstance(): Retrofit {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
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