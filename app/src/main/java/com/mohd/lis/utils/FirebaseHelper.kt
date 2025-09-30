package com.mohd.lis.utils

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.mohd.lis.roomDB.NotePojo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object FirebaseHelper {

    fun database() = FirebaseFirestore.getInstance()

    fun getAllNotesFromFireStore(
        deviceId: String,
        onResult: (List<NotePojo>) -> Unit
    ): FirebaseHelper {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val notesRef = database().collection("notes")
                    .document(deviceId)
                    .collection("user_notes")

                notesRef.addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.d(
                            "getAllNotesFromFireStore: ",
                            "Error fetching notes: ${error.message}"
                        )
                        return@addSnapshotListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        val notesList = snapshot.documents.mapNotNull { doc ->
                            doc.toObject(NotePojo::class.java)
                        }
                        onResult(notesList)
                    } else {
                        onResult(emptyList())
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                Log.d("getAllNotesFromFireStore: ", exception.message.toString())
            }
        }
        return this
    }

    fun insertOrUpdateNoteToFirebase(
        notePojo: NotePojo,
        onResult: (Boolean) -> Unit
    ): FirebaseHelper {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val notesRef = database().collection("notes")
                    .document(notePojo.deviceId ?: "unknown")
                    .collection("user_notes")
                    .document(notePojo.id.toString())

                notesRef.set(notePojo).addOnSuccessListener {
                    onResult(true)
                    Log.d(
                        "insertNoteToFirebase-addOnSuccessListener: ",
                        "Note Uploaded Successfully"
                    )
                }.addOnFailureListener {
                    onResult(false)
                    Log.d("insertNoteToFirebase-addOnFailureListener: ", "Failed")
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                Log.d("insertNoteToFirebase: ", exception.message.toString())
            }
        }
        return this
    }

    fun deleteNoteFromFirestore(notePojo: NotePojo, onResult: (Boolean) -> Unit): FirebaseHelper {
        try {
            val notesRef = database().collection("notes")
                .document(notePojo.deviceId ?: "unknown")
                .collection("user_notes")
                .document(notePojo.id.toString())

            notesRef.delete().addOnSuccessListener {
                onResult(true)
                Log.d("deleteNoteFromFirebase", "Note Deleted Successfully")
            }.addOnFailureListener { exception ->
                onResult(false)
                Log.e("deleteNoteFromFirebase", "Failed: ${exception.message}", exception)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            Log.e("deleteNoteFromFirestore: ", exception.message.toString(), exception)
        }
        return this
    }
}