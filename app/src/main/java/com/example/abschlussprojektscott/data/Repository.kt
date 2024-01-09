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

    // private val name = "Berlin"
    private val key = "1dd7de79eba41239266b10812486bd02"
    private val lon: Double = 13.4105
    private val lat: Double = 52.5244

    private var _weatherData = MutableLiveData<WeatherData>()
    val weatherData: LiveData<WeatherData>
        get() = _weatherData

    var notes: LiveData<MutableList<Notes>> = database.noteDao.getAll()

    private val _selectedNote = MutableLiveData<Notes>()
    val selectedNote: LiveData<Notes>
        get() = _selectedNote

    suspend fun getSelectedNoteById(id: Long) {
        try {
            _selectedNote.value = database.noteDao.getSelectedNoteById(id)
        } catch (e: Exception) {
            Log.e("Repository", "Note could not be selected")
        }
    }

    suspend fun getWeatherData() {
        try {
            val result = api.retrofitService.getWeatherData(lat, lon, key)
            _weatherData.postValue(result)
        } catch (e: Exception) {
            Log.e("Repository-getWeatherData", "Api could not be loaded")
        }
    }

    fun getNotes() {
        try {
            notes = database.noteDao.getAll() as MutableLiveData<MutableList<Notes>>
        } catch (e: Exception) {
            Log.e("Repository-getNotes", "Notes could not be loaded")
        }
    }


    suspend fun insertNote(note: Notes) {
        try {
            database.noteDao.insertNote(note)
        } catch (e: Exception) {
            Log.e("Repository-InsertNote", "Note could not be added")
        }
    }

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