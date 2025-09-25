package com.mohd.lis.appBase

import NetworkManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

abstract class AppBaseActivity: AppCompatActivity(), NetworkManager.NetworkListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NetworkManager.setNetworkListener(this)
    }

    override fun onNetworkChanged(isConnected: Boolean) {
        if (isConnected) {
            Log.d("NetworkManager", "Network is available.")
        } else {
            Log.d("NetworkManager", "Network is lost.")
        }
    }
}