package com.example.abschlussprojektscott.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.abschlussprojektscott.data.model.Notes

@Dao
interface ScottsDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(noteItem: Notes)

    @Query("SELECT * FROM note_table")
    fun getAll(): LiveData<MutableList<Notes>>

    @Query("DELETE FROM note_table WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: Long)

    @Query("SELECT * FROM note_table WHERE id = :id")
    suspend fun getSelectedNoteById(id: Long): Notes

}