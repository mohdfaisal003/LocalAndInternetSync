package com.mohd.lis.viewModels

/* This Project is Made By Mohd Faisal For Collaboration/Projects Please feel Free to Connect over fpecial3@gmail.com
Portfolio: https://mohdfaisal.web.app/ */

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohd.lis.roomDB.NotePojo
import com.mohd.lis.roomDB.NotesDatabase
import com.mohd.lis.roomDB.NotesRepo
import com.mohd.lis.utils.FirebaseHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val notesRepo: NotesRepo by lazy {
        NotesRepo(NotesDatabase.getInstance(application).notesDao())
    }

    val allNotes: LiveData<List<NotePojo>> = notesRepo.allNotes
    fun insertNote(notePojo: NotePojo) = viewModelScope.launch(Dispatchers.IO) {
        try {
            notesRepo.insertNote(notePojo)
        } catch (exception: Exception) {
            exception.printStackTrace()
            Log.e("insertNote: ", exception.message.toString())
        }
    }

    fun updateNote(notesPojo: NotePojo, isConnected: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("updateNote: ", notesPojo.toString())
                Log.d("updateNote: ", isConnected.toString())
                if (isConnected) {
                    FirebaseHelper.insertOrUpdateNoteToFirebase(
                        notesPojo.copy(isUploaded = true),
                        { isUploaded ->
                            CoroutineScope(Dispatchers.IO).launch {
                                if (isUploaded) {
                                    notesRepo.updateNote(notesPojo)
                                }
                            }
                        })
                } else {
                    notesRepo.updateNote(notesPojo)
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                Log.e("updateNote: ", exception.message.toString())
            }
        }

    fun deleteNote(isConnected: Boolean, notesPojo: NotePojo) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (!isConnected) {
                    notesRepo.deleteNote(notesPojo)
                } else {
                    FirebaseHelper.deleteNoteFromFirestore(notesPojo, { isDeleted ->
                        CoroutineScope(Dispatchers.IO).launch {
                            if (isDeleted) {
                                notesRepo.deleteNote(notesPojo)
                            }
                        }
                    })
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                Log.e("deleteNote: ", exception.message.toString())
            }
        }

    suspend fun getNoteById(id: Int): NotePojo? {
        return notesRepo.getNoteById(id)
    }
}