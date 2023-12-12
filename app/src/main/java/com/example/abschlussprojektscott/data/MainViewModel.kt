package com.example.abschlussprojektscott.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.abschlussprojektscott.data.remote.ScottsApi
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = Repository(ScottsApi)

    fun getWeatherResult() {
        viewModelScope.launch {
            repo.getWeatherResult()
        }
    }

}