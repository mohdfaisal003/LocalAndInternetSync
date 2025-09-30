package com.mohd.lis.adapters

/* This Project is Made By Mohd Faisal For Collaboration/Projects Please feel Free to Connect over fpecial3@gmail.com
Portfolio: https://mohdfaisal.web.app/ */

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mohd.lis.databinding.RvNoteLayoutBinding
import com.mohd.lis.roomDB.NotePojo
import com.mohd.lis.ui.UpdateNoteActivity
import com.mohd.lis.utils.AppUtil
import com.mohd.lis.viewModels.NotesViewModel

class NotesAdapter(private val context: Context, private val notesViewModel: NotesViewModel) :
    ListAdapter<NotePojo, NotesAdapter.ViewHolder>(NotesDiffUtilCallBack()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            RvNoteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: RvNoteLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var isConnected = false;

        fun bind(notePojo: NotePojo) {
            NetworkManager.networkLiveData.observe(context as AppCompatActivity) {
                isConnected = it
            }
            Log.d("bind: ", isConnected.toString())
            binding.titleTv.text = notePojo.title.toString()
            binding.descTv.text = notePojo.desc.toString()
            binding.timeTv.text = notePojo.time.toString()
            if (notePojo.isUploaded) {
                binding.statusTv.text = "Uploaded"
                binding.statusTv.setTextColor(
                    binding.root.context.applicationContext.getColor(
                        android.R.color.holo_green_light
                    )
                )
            } else {
                binding.statusTv.text = "Not Uploaded"
                binding.statusTv.setTextColor(
                    binding.root.context.applicationContext.getColor(
                        android.R.color.holo_red_light
                    )
                )
            }

            binding.mainCard.setOnClickListener {
                val intent = Intent(binding.root.context, UpdateNoteActivity::class.java)
                intent.putExtra("noteId", notePojo.id)
                binding.root.context.startActivity(intent)
            }

            binding.mainCard.setOnLongClickListener(object : View.OnLongClickListener {
                override fun onLongClick(view: View?): Boolean {
                    showDeleteAlert(binding.root.context, notePojo)
                    return true
                }
            })
        }

        fun showDeleteAlert(context: Context, note: NotePojo) {
            AlertDialog.Builder(context)
                .setTitle(note.title)
                .setMessage("Do you want to delete this note?")
                .setPositiveButton("Delete") { dialog, which ->
                    notesViewModel.deleteNote(isConnected, note)
                    dialog?.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, which -> dialog?.dismiss() }
                .show()
        }
    }
}

class NotesDiffUtilCallBack : DiffUtil.ItemCallback<NotePojo>() {
    override fun areItemsTheSame(
        oldItem: NotePojo,
        newItem: NotePojo
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: NotePojo,
        newItem: NotePojo
    ): Boolean {
        return oldItem == newItem
    }
}