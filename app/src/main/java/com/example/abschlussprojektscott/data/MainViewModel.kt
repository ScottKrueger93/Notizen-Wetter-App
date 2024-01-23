package com.example.abschlussprojektscott.data

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.abschlussprojektscott.data.local.NoteDatabase.Companion.getDatabase
import com.example.abschlussprojektscott.data.model.Note
import com.example.abschlussprojektscott.data.model.Notes
import com.example.abschlussprojektscott.data.remote.ScottsApi
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application) {

    //Bezüge zu den jeweiligen Dateien werden erstellt
    private val database = getDatabase(application)
    private val repo = Repository(ScottsApi, database)

    //Bezüge zu den Live-Data werden erstellt
    val selectedNote = repo.selectedNote
    val notes = repo.notes
    private val weatherData = repo.weatherData

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
    fun getWeatherData(lati:Double, loni: Double) {
        viewModelScope.launch {
            try {
                repo.getWeatherData(lati, loni)
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseDateTime(dateTimeString: String): LocalDateTime {
        val patterns = arrayOf(
            "dd.MM.yyyy HH:mm",
            "dd.MM.yyyy H:mm",
            "dd.MM.yyyy HH:mm:ss",
            "dd.MM.yyyy H:mm:ss",
            "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd H:mm",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd H:mm:ss",
            "MM/dd/yyyy HH:mm",
            "MM/dd/yyyy H:mm",
            "MM/dd/yyyy HH:mm:ss",
            "MM/dd/yyyy H:mm:ss",
        )

        for (pattern in patterns) {
            try {
                return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern(pattern))
            } catch (e: DateTimeParseException) {
                // Ignorieren und mit dem nächsten Muster versuchen
            }
        }

        // Standardwert, wenn keine der Formate passt (könnte eine Ausnahmebehandlung oder ein anderer Standardwert sein)
        return LocalDateTime.now()
    }

    fun validateTimeFormat(inputTime: String): String {
        val validFormats = listOf("HH:mm", "H:mm")

        for (format in validFormats) {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            sdf.isLenient = false // Die Überprüfung ist nicht flexibel

            try {
                sdf.parse(inputTime)?.let {
                    // Die Eingabe hat eines der erwarteten Zeitformate
                    return inputTime
                }
            } catch (e: ParseException) {
                // Ignoriere die Ausnahme und versuche das nächste Format
            }
        }

        // Keines der erwarteten Formate wurde gefunden
        return "Wrong Input"
    }

    fun validateDateFormat(inputDate: String): String {
        val validFormats = listOf(
            "dd.MM.yyyy",
            "yyyy-MM-dd",
            "MM/dd/yyyy",
        )

        for (format in validFormats) {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            sdf.isLenient = false // Die Überprüfung ist nicht flexibel

            try {
                sdf.parse(inputDate)?.let {
                    // Die Eingabe hat eines der erwarteten Zeitformate
                    return inputDate
                }
            } catch (e: ParseException) {
                // Ignoriere die Ausnahme und versuche das nächste Format
            }
        }

        // Keines der erwarteten Formate wurde gefunden
        return "Wrong Input"
    }

}