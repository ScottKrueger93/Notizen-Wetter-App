package com.example.abschlussprojektscott.data.model

import androidx.room.PrimaryKey

data class Note(
    var id: Long = 0,
    var name: String,
    var date: String,
    var time: String,
    var description: String
) {
}
