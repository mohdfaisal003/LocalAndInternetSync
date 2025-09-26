package com.mohd.lis.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohd.lis.roomDB.NotePojo
import com.mohd.lis.roomDB.NotesDatabase
import com.mohd.lis.roomDB.NotesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(application: Application): AndroidViewModel(application) {

    private val notesRepo: NotesRepo by lazy {
        NotesRepo(NotesDatabase.getInstance(application).notesDao())
    }

    val allNotes: LiveData<List<NotePojo>> = notesRepo.allNotes
    fun insertNote(notePojo: NotePojo) = viewModelScope.launch(Dispatchers.IO) {
        try {
            notesRepo.insertNote(notePojo)
        } catch (exception: Exception) {
            exception.printStackTrace()
            Log.e( "insertNote: ", exception.message.toString())
        }
    }

    fun updateNote(notesPojo: NotePojo) = viewModelScope.launch(Dispatchers.IO) {
        try {
            notesRepo.updateNote(notesPojo)
        } catch (exception: Exception) {
            exception.printStackTrace()
            Log.e( "updateNote: ", exception.message.toString())
        }
    }

    fun deleteNote(notesPojo: NotePojo) = viewModelScope.launch(Dispatchers.IO) {
        try {
            notesRepo.deleteNote(notesPojo)
        } catch (exception: Exception) {
            exception.printStackTrace()
            Log.e( "deleteNote: ", exception.message.toString())
        }
    }

    suspend fun getNoteById(id: Int): NotePojo? {
        return notesRepo.getNoteById(id)
    }
}