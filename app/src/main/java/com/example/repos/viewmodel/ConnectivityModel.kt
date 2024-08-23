package com.example.repos.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.*


class ConnectivityModel(application: Application) : AndroidViewModel(application), LifecycleOwner {
    private val TAG = ConnectivityModel::class.simpleName
    var status = MutableLiveData<Boolean>()

    @SuppressLint("StaticFieldLeak")
    private var lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    init {
        start()

    }


    private fun start() {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerNetworkCallback(
            NetworkRequest.Builder().build(),
            object : ConnectivityManager.NetworkCallback() {
                /**
                 * @param network
                 */
                override fun onAvailable(network: Network) {
                    Log.e(TAG, "onAvailable: ")
                    status.postValue(true)


                }

                /**
                 * @param network
                 */
                override fun onLost(network: Network) {
                    Log.e(TAG, "onLost: ")
                    status.postValue(false)


                }
            })


    }


    override fun onCleared() {
        super.onCleared()
       // lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry
}