package com.mohd.lis

import NetworkManager
import android.app.Application
import com.google.firebase.FirebaseApp

class LocalAndInternetSync : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        NetworkManager.startNetworkMonitoring(this)
    }
}