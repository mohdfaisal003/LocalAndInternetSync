import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/* This Project is Made By Mohd Faisal For Collaboration/Projects Please feel Free to Connect over fpecial3@gmail.com
Portfolio: https://mohdfaisal.web.app/ */

object NetworkManager {

    private var connectivityManager: ConnectivityManager? = null
    private var isMonitoring = false
    private val _networkLiveData = MutableLiveData<Boolean>()
    val networkLiveData: LiveData<Boolean> get() = _networkLiveData

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            Log.d("NetworkManager", "Network available")
            _networkLiveData.postValue(true)
        }

        override fun onLost(network: Network) {
            Log.d("NetworkManager", "Network lost")
            _networkLiveData.postValue(false)
        }
    }

    fun startNetworkMonitoring(context: Context) {
        if (!isMonitoring) {
            connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            connectivityManager?.registerNetworkCallback(networkRequest, networkCallback)
            isMonitoring = true
            Log.d("NetworkManager", "Network monitoring started.")

            val activeNetwork = connectivityManager?.activeNetwork
            val capabilities = connectivityManager?.getNetworkCapabilities(activeNetwork)
            val connected = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
            Log.d("NetworkManager", connected.toString())
            _networkLiveData.postValue(connected)
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