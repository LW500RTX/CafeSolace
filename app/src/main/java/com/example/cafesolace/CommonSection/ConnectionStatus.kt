package com.example.cafesolace.CommonSection

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Enum class representing different network connection states
enum class ConnectionStatus {
    Available,   // Network is available and connected
    Unavailable, // No network connectivity
    Losing,      // Network connection is about to be lost
    Lost         // Network connection has been lost
}

// Class to observe network connectivity changes using Android's ConnectivityManager
class ConnectivityObserver(context: Context) {

    // Get system ConnectivityManager from context
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // MutableStateFlow to hold the current connection status; starts as Unavailable
    private val _connectionStatus = MutableStateFlow(ConnectionStatus.Unavailable)
    // Public immutable StateFlow to expose connection status to observers
    val connectionStatus: StateFlow<ConnectionStatus> = _connectionStatus.asStateFlow()

    // NetworkCallback to listen to connectivity events from the system
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        // Called when network becomes available
        override fun onAvailable(network: Network) {
            _connectionStatus.value = ConnectionStatus.Available
        }

        // Called when network is lost/disconnected
        override fun onLost(network: Network) {
            _connectionStatus.value = ConnectionStatus.Lost
        }

        // Called when network is about to be lost soon
        override fun onLosing(network: Network, maxMsToLive: Int) {
            _connectionStatus.value = ConnectionStatus.Losing
        }

        // Called when no network is available (rare, but possible)
        override fun onUnavailable() {
            _connectionStatus.value = ConnectionStatus.Unavailable
        }
    }

    init {
        // Build a default network request to listen for all types of networks
        val request = NetworkRequest.Builder().build()
        // Register the callback to start receiving network updates
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    // Call this method to unregister the callback when the observer is no longer needed,
    // to prevent memory leaks and stop listening for connectivity changes
    fun unregisterCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}
