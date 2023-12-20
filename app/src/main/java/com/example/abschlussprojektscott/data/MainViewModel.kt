package com.example.abschlussprojektscott.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.abschlussprojektscott.data.local.NoteDatabase.Companion.getDatabase
import com.example.abschlussprojektscott.data.model.Note
import com.example.abschlussprojektscott.data.model.Notes
import com.example.abschlussprojektscott.data.model.Weather
import com.example.abschlussprojektscott.data.model.WeatherData
import com.example.abschlussprojektscott.data.remote.ScottsApi
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repo = Repository(ScottsApi, database)

    val notes = repo.notes
    val weatherData = repo.weatherData

    fun getWeatherData() {
        viewModelScope.launch {
            try {
                repo.getWeatherData()
            } catch (e: Exception) {
                Log.e("MainViewModel-getWeatherData", "could not load WeatherData")
            }

        }
    }

    fun getNotes() {
        viewModelScope.launch {
            try {
                repo.getNotes()
            } catch (e: Exception) {
                Log.e("MainViewModel-getNotes", "could")
            }

        }
    }

    fun insertNote(note: Note) {
        viewModelScope.launch {
            try {
                val notes = Notes(
                    noteName = note.name,
                    noteDate = note.date,
                    noteTime = note.time,
                    noteDescription = note.description,

                    weatherName = weatherData.value?.weather?.first()!!.main,
                    weatherDescription = weatherData.value?.weather?.first()!!.description,
                    weatherIcon = weatherData.value?.weather?.first()!!.icon,
                )
                repo.insertNote(notes)
            } catch (e: Exception) {
                Log.e("MainViewModel-insertNote", "Note could not be added")
            }
        }
    }

    fun deleteNote(notes: Notes) {
        viewModelScope.launch {
            try {
                repo.deleteNote(notes)
            } catch (e: Exception) {
                Log.e("MainViewModel-deleteNote", "")
            }

        }
    }


}