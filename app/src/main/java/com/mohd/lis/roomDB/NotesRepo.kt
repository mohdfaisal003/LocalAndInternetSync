package com.mohd.lis.roomDB

import androidx.lifecycle.LiveData

class NotesRepo(private val notesDao: NotesDao) {
    val allNotes: LiveData<List<NotePojo>> = notesDao.getAllNotes()
    suspend fun insertNote(notePojo: NotePojo) {
        notesDao.insertNote(notePojo)
    }

    suspend fun updateNote(notePojo: NotePojo) {
        notesDao.updateNote(notePojo)
    }

    suspend fun deleteNote(notePojo: NotePojo) {
        notesDao.deleteNote(notePojo)
    }

    suspend fun getNoteById(id: Int): NotePojo? {
        return notesDao.getNoteById(id)
    }
}