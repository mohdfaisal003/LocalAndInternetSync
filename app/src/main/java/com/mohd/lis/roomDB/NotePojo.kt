package com.mohd.lis.roomDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class NotePojo(
    val time: String?,
    val title: String?,
    val desc: String?,
    val deviceId: String?,
    val isUploaded: Boolean,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
