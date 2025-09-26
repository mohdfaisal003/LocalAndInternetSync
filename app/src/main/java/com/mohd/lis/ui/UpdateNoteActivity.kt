package com.mohd.lis.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.mohd.lis.R
import com.mohd.lis.appBase.AppBaseActivity
import com.mohd.lis.databinding.ActivityUpdateNoteBinding
import com.mohd.lis.roomDB.NotePojo
import com.mohd.lis.utils.AppUtil
import com.mohd.lis.viewModels.NotesViewModel
import kotlinx.coroutines.launch

class UpdateNoteActivity: AppBaseActivity() {

    private lateinit var binding: ActivityUpdateNoteBinding
    private val notesViewModel: NotesViewModel by viewModels()
    private var noteId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.updateNoteToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Update Note"

        noteId = intent.getIntExtra("noteId",0)
        Log.d("onCreate: ",noteId.toString())
        lifecycleScope.launch {
            notesViewModel.getNoteById(noteId)?.let {
                Log.d("onCreate: ",it.desc.toString())
                binding.contentEt.setText(it.desc)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.notes_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                if (!binding.contentEt.text.isNullOrEmpty()) {
                    saveNote()
                } else {
                    AppUtil.showMessage(this,"Please write something")
                }
                true
            }

            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveNote() {
        val desc = binding.contentEt.text.toString()
        val title = desc.lineSequence().firstOrNull()?.trim() ?: ""
        notesViewModel.updateNote(
            NotePojo(
                AppUtil.getTime(this),
                title,
                desc,
                AppUtil.getDeviceId(this),
                false,
                noteId
            )
        )
        finish()
    }
}