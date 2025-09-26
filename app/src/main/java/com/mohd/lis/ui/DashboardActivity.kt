package com.mohd.lis.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohd.lis.R
import com.mohd.lis.adapters.NotesAdapter
import com.mohd.lis.appBase.AppBaseActivity
import com.mohd.lis.databinding.ActivityDashboardBinding
import com.mohd.lis.viewModels.NotesViewModel

class DashboardActivity : AppBaseActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private val notesViewModel by viewModels<NotesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = NotesAdapter(notesViewModel)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        binding.recyclerView.adapter = adapter

        notesViewModel.allNotes.observe(this) { notes ->
            adapter.submitList(notes)
        }

        binding.addNewNoteIB.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }
    }
}