package com.example.abschlussprojektscott.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abschlussprojektscott.data.local.NoteDatabase
import com.example.abschlussprojektscott.data.model.Notes
import com.example.abschlussprojektscott.data.model.Weather
import com.example.abschlussprojektscott.data.model.WeatherData
import com.example.abschlussprojektscott.data.remote.ScottsApi

class Repository(private val api: ScottsApi, private val database: NoteDatabase) {

    //Api-Key
    private val key = "1dd7de79eba41239266b10812486bd02"

    //Längen- und Breitengrade von Berlin
    private val lon: Double = 13.4105
    private val lat: Double = 52.5244

    //Live-Data zum Beobachten aller Wetterdaten des Api-Calls
    private var _weatherData = MutableLiveData<WeatherData>()
    val weatherData: LiveData<WeatherData>
        get() = _weatherData

    //Live-Data zum beobachten aller in der Datenbank eingetragenen Notizen
    var notes: LiveData<MutableList<Notes>> = database.noteDao.getAll()

    //Live-Data zum beobachtenn der ausgewählten Notiz
    private val _selectedNote = MutableLiveData<Notes>()
    val selectedNote: LiveData<Notes>
        get() = _selectedNote

    //Funktion zum Abrufen der aktuellen ID der ausgewählten Notiz
    suspend fun getSelectedNoteById(id: Long) {
        try {
            _selectedNote.value = database.noteDao.getSelectedNoteById(id)
        } catch (e: Exception) {
            Log.e("Repository", "Note could not be selected")
        }
    }

    //Funktion zum Abrufen aller Wetterdaten aus dem Api-Call
    suspend fun getWeatherData() {
        try {
            val result = api.retrofitService.getWeatherData(lat, lon, key)
            _weatherData.postValue(result)
        } catch (e: Exception) {
            Log.e("Repository-getWeatherData", "Api could not be loaded")
        }
    }

    //Funktion zum Abrufen aller Notizen aus der Datenbank
    fun getNotes() {
        try {
            notes = database.noteDao.getAll() as MutableLiveData<MutableList<Notes>>
        } catch (e: Exception) {
            Log.e("Repository-getNotes", "Notes could not be loaded")
        }
    }

    //Funktion zum einfügen oder ersätzen einer Notiz in der Datenbank
    suspend fun insertNote(note: Notes) {
        try {
            database.noteDao.insertNote(note)
        } catch (e: Exception) {
            Log.e("Repository-InsertNote", "Note could not be added")
        }
    }

    //Funktion zum löschen von Notizen in der Datenbank
    suspend fun deleteNoteById(noteId: Long) {
        try {
            Log.d("Repository-Delete", "Deleting note with ID: $noteId")
            database.noteDao.deleteNoteById(noteId)
            Log.d("Repository-Delete", "Note deleted successfully")
        } catch (e: Exception) {
            Log.e("Repository-Delete", "Note could not be deleted: ${e.message}")
        }
    }

}