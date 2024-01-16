package com.example.abschlussprojektscott.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.abschlussprojektscott.data.local.NoteDatabase.Companion.getDatabase
import com.example.abschlussprojektscott.data.model.Note
import com.example.abschlussprojektscott.data.model.Notes
import com.example.abschlussprojektscott.data.model.Weather
import com.example.abschlussprojektscott.data.model.WeatherData
import com.example.abschlussprojektscott.data.remote.ScottsApi
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    //Bezüge zu den jeweiligen Dateien werden erstellt
    private val database = getDatabase(application)
    private val repo = Repository(ScottsApi, database)

    //Bezüge zu den Live-Data werden erstellt
    val selectedNote = repo.selectedNote
    val notes = repo.notes
    val weatherData = repo.weatherData

    //Coroutine für die Funktion aus dem Repository wird erstellt
    fun getSelectedNoteById(id: Long) {
        viewModelScope.launch {
            repo.getSelectedNoteById(id)
        }
    }

    //Fügt die Eingaben des Nutzers mit den Daten aus dem API-Call zur Datenbank hinzu und überschreibt einen Vorhandenen Eintrag
    fun updateNote(note: Note) {
        viewModelScope.launch {
            try {
                val notes = Notes(
                    id = note.id,
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
                Log.e("MainViewModel-updateNote", "Note could not be updated")
            }
        }
    }

    //Coroutine zur getWeatherData-Funktion aus dem Repository
    fun getWeatherData() {
        viewModelScope.launch {
            try {
                repo.getWeatherData()
            } catch (e: Exception) {
                Log.e("MainViewModel-getWeatherData", "could not load WeatherData")
            }
        }
    }

    //Coroutine zur getNotes-Funktion aus dem Repository
    fun getNotes() {
        viewModelScope.launch {
            try {
                repo.getNotes()
            } catch (e: Exception) {
                Log.e("MainViewModel-getNotes", "Notes could not be loaded")
            }
        }
    }

    //
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

    fun deleteNoteById(notes: Long) {
        viewModelScope.launch {
            try {
                repo.deleteNoteById(notes)
            } catch (e: Exception) {
                Log.e("MainViewModel-deleteNote", "Note could not be deleted")
            }
        }
    }


}