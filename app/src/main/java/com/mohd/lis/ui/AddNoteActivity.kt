package com.mohd.lis.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mohd.lis.R
import com.mohd.lis.databinding.ActivityAddNoteBinding
import com.mohd.lis.roomDB.NotePojo
import com.mohd.lis.utils.AppUtil
import com.mohd.lis.viewModels.NotesViewModel

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private val notesViewModel by viewModels<NotesViewModel>()
    private var isConnected = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NetworkManager.networkLiveData.observe(this) { isConnected ->
            this.isConnected = isConnected
            Log.d( "initialize-AddNote: ", isConnected.toString())
        }

        setSupportActionBar(binding.addNoteToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add Note"
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
        notesViewModel.insertNote(
            NotePojo(
                AppUtil.getTime(this),
                title,
                desc,
                AppUtil.getDeviceId(this),
                false
            )
        )
        finish()
    }
}