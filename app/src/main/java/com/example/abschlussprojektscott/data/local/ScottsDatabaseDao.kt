package com.example.abschlussprojektscott.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.abschlussprojektscott.data.model.Notes

@Dao
interface ScottsDatabaseDao {

    //Funktion zum Anlegen und überschreiben einer Notiz
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(noteItem: Notes)

    //Funktion um die Daten der Datenbank zu laden oder zu aktualisieren
    @Query("SELECT * FROM note_table")
    fun getAll(): LiveData<MutableList<Notes>>

    //Funktion um eine Notiz zu löschen
    @Query("DELETE FROM note_table WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: Long)

    //Funktion um die ID der angeklickten Notiz zu bekommen
    @Query("SELECT * FROM note_table WHERE id = :id")
    suspend fun getSelectedNoteById(id: Long): Notes

}