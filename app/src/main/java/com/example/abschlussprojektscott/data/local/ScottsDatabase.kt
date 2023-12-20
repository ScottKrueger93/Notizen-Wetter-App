package com.example.abschlussprojektscott.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.abschlussprojektscott.data.model.Notes

@Database(entities = [Notes::class], version = 2)
abstract class NoteDatabase : RoomDatabase() {

    abstract val noteDao: ScottsDatabaseDao

    companion object {
        private const val DATABASE_NAME = "notes_database"

        // Migration von Version 1 auf Version 2
        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Hier können SQL-Anweisungen für die Migration eingefügt werden, falls erforderlich.
            }
        }

        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    DATABASE_NAME
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
