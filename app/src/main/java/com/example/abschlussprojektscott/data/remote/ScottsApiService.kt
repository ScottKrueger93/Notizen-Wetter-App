package com.example.abschlussprojektscott.data.remote

import com.example.abschlussprojektscott.data.model.WeatherData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://api.openweathermap.org"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ScottsApiService {

    //https://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}
    //
    // key=1dd7de79eba41239266b10812486bd02

    @GET("/data/2.5/weather")
    suspend fun getWeatherResult(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") key: String
    ): WeatherData

}

object ScottsApi {
    val retrofitService: ScottsApiService by lazy { retrofit.create(ScottsApiService::class.java) }
}