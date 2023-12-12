package com.example.abschlussprojektscott.data

import androidx.lifecycle.MutableLiveData
import com.example.abschlussprojektscott.data.model.WeatherData
import com.example.abschlussprojektscott.data.remote.ScottsApi

class Repository(private val api: ScottsApi) {

   // private val name = "Berlin"
    private val key = "1dd7de79eba41239266b10812486bd02"
    private val lon: Double = 13.4105
    private val lat: Double = 52.5244

    private val _weatherResult = MutableLiveData<WeatherData>()

    suspend fun getWeatherResult() {

        val result = api.retrofitService.getWeatherResult(lat, lon, key)
        _weatherResult.postValue(result)
//        try {
//            val result = api.retrofitService.getWeatherResult(name, key)
//            _weatherResult.postValue(result)
//        } catch (e: Exception) {
//            Log.e("Api", "Laden der WeatherResult nicht möglich")
//        }
    }
}