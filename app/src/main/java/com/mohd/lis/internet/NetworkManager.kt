import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log

object NetworkManager {

    private var connectivityManager: ConnectivityManager? = null
    private var isMonitoring = false
    interface NetworkListener {
        fun onNetworkChanged(isConnected: Boolean)
    }
    private var networkListener: NetworkListener? = null
    fun setNetworkListener(listener: NetworkListener) {
        this.networkListener = listener
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            networkListener?.onNetworkChanged(true)
        }

        override fun onLost(network: Network) {
            networkListener?.onNetworkChanged(false)
        }
    }

    fun startNetworkMonitoring(context: Context) {
        if (!isMonitoring) {
            connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            connectivityManager?.requestNetwork(networkRequest, networkCallback)
            isMonitoring = true
            Log.d("NetworkManager", "Network monitoring started.")
        }
    }

    fun stopNetworkMonitoring() {
        if (isMonitoring) {
            connectivityManager?.unregisterNetworkCallback(networkCallback)
            isMonitoring = false
            Log.d("NetworkManager", "Network monitoring stopped.")
        }
    }
}