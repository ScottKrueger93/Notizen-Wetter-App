package com.example.abschlussprojektscott.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.abschlussprojektscott.data.model.Notes

@Database(entities = [Notes::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract val noteDao: ScottsDatabaseDao
}

private lateinit var dbInstance: NoteDatabase
fun getDatabase(context: Context): NoteDatabase {
    synchronized(NoteDatabase::class.java) {
        if (!::dbInstance.isInitialized) {
            dbInstance = Room.databaseBuilder(
                context.applicationContext,
                NoteDatabase::class.java,
                "notes_database"
            ).build()
        }
        return dbInstance
    }
}