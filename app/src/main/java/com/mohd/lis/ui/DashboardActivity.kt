package com.mohd.lis.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohd.lis.adapters.NotesAdapter
import com.mohd.lis.databinding.ActivityDashboardBinding
import com.mohd.lis.utils.FirebaseHelper
import com.mohd.lis.viewModels.NotesViewModel

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private val notesViewModel by viewModels<NotesViewModel>()
    private lateinit var adapter: NotesAdapter
    private var isConnected = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = NotesAdapter(this,notesViewModel)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        binding.recyclerView.adapter = adapter

        NetworkManager.networkLiveData.observe(this) { isConnected ->
            this.isConnected = isConnected
            Log.d( "initialize-Dashboard: ", isConnected.toString())
            if (isConnected || !isConnected) {
                setNotes()
            }
        }

        binding.addNewNoteIB.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setNotes() {
        notesViewModel.allNotes.observe(this) { notes ->
            adapter.submitList(notes)
            val pendingNote = notes.filter { !it.isUploaded }
            if (pendingNote.isNotEmpty() && isConnected) {
                pendingNote.forEach { note ->
                    FirebaseHelper.insertOrUpdateNoteToFirebase(note.copy(isUploaded = true), { isUploaded ->
                        if (isUploaded) {
                            notesViewModel.updateNote(note.copy(isUploaded = true),false)
                        }
                    })
                }
            }
        }
    }
}