package com.mohd.lis.roomDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NotePojo)

    @Update
    suspend fun updateNote(note: NotePojo)

    @Delete
    suspend fun deleteNote(note: NotePojo)

    @Query("SELECT * FROM notes_table WHERE id = :id")
    suspend fun getNoteById(id: Int): NotePojo?

    @Query("SELECT * FROM notes_table")
    fun getAllNotes(): LiveData<List<NotePojo>>
}