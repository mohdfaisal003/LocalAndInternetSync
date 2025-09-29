package com.mohd.lis.internet

interface NetworkListener {
    fun onNetworkChanged(isConnected: Boolean)
}