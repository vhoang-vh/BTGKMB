package com.example.bit240102_foodmanager

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import com.example.bit240102_foodmanager.data.FoodDatabase
import com.example.bit240102_foodmanager.services.FoodRepository
import com.example.bit240102_foodmanager.services.MockApiServiceFactory
import com.example.bit240102_foodmanager.services.SyncScheduler

class FoodManagerApplication : Application() {
    val database: FoodDatabase by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        FoodDatabase.create(this)
    }

    val repository: FoodRepository by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        FoodRepository(this, database)
    }

    override fun onCreate() {
        super.onCreate()
        if (!MockApiServiceFactory.isConfigured(this)) return

        val manager = getSystemService(ConnectivityManager::class.java)
        try {
            manager.registerNetworkCallback(
                NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build(),
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        SyncScheduler.schedule(this@FoodManagerApplication)
                    }
                },
            )
            SyncScheduler.schedule(this)
        } catch (error: SecurityException) {
            Log.e("BIT240102_Sync", "Could not monitor Internet connectivity.", error)
        }
    }
}
