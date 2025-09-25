package com.mohd.lis

import android.app.Application

class LocalAndInternetSync: Application() {
    override fun onCreate() {
        super.onCreate()
        NetworkManager.startNetworkMonitoring(this)
    }
}