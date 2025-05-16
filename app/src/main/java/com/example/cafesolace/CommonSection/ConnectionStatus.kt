package com.example.cafesolace.CommonSection

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ConnectionStatus {
    Available, Unavailable, Losing, Lost
}

class ConnectivityObserver(context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _connectionStatus = MutableStateFlow(ConnectionStatus.Unavailable)
    val connectionStatus: StateFlow<ConnectionStatus> = _connectionStatus.asStateFlow()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _connectionStatus.value = ConnectionStatus.Available
        }

        override fun onLost(network: Network) {
            _connectionStatus.value = ConnectionStatus.Lost
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            _connectionStatus.value = ConnectionStatus.Losing
        }

        override fun onUnavailable() {
            _connectionStatus.value = ConnectionStatus.Unavailable
        }
    }

    init {
        val request = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    // ✅ Add this function to clean up when no longer needed
    fun unregisterCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}
