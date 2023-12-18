package com.example.abschlussprojektscott.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.abschlussprojektscott.data.local.getDatabase
import com.example.abschlussprojektscott.data.model.Notes
import com.example.abschlussprojektscott.data.model.Weather
import com.example.abschlussprojektscott.data.model.WeatherData
import com.example.abschlussprojektscott.data.remote.ScottsApi
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repo = Repository(ScottsApi, database)

    val note = repo.notes
    val weather = repo.weatherData

    fun getWeatherData() {
        viewModelScope.launch {
            repo.getWeatherData()
        }
    }

    fun getNote() {
        viewModelScope.launch {
            repo.getNotes()
        }
    }

    fun insertNote(notes: Notes) {
        viewModelScope.launch {
            try {
                val note = Notes(
                    noteName = notes.noteName,
                    noteDate = notes.noteDate,
                    noteDescription = notes.noteDescription,


                    weatherName = weather.value.weather.first().main,
                    weatherDescription = weather.description,
                    weatherIcon = weather.icon,
                )
                repo.insertNote(note)
            } catch (e: Exception) {
                Log.e("MainViewModel-insertNote", "Note could not be added")
            }
        }
    }

    fun deleteNote(notes: Notes) {
        viewModelScope.launch {
            repo.deleteNote(notes)
        }
    }


}