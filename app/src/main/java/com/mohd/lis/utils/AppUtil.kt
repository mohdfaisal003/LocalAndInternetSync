package com.mohd.lis.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.mohd.lis.roomDB.NotePojo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AppUtil {

    fun getTime(context: Context): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date(System.currentTimeMillis()))
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun showMessage(context: Context,msg: String?) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }
}