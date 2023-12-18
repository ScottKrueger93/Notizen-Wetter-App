package com.example.abschlussprojektscott.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("note_table")
data class Notes(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var noteName: String,
    var noteDate: String,
    var noteDescription: String,
    var weatherName: String,
    var weatherDescription: String,
    var weatherIcon: String
)