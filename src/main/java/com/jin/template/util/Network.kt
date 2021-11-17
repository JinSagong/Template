package com.jin.template.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.jin.template.TemplateController.appContext
import com.jin.template.TemplateController.offlineMsg

object Network {
    private val cm by lazy { appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }

    fun checkConnection(showToast: Boolean = true) = check().also {
        if (!it && showToast) try {
            Toasty.showAlert(offlineMsg)
        } catch (e: Exception) {
        }
    }

    @Suppress("DEPRECATION")
    private fun check(): Boolean {
        var result = false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Debug.i("[Network] cm.activityNetwork=${cm.activeNetwork}")
            val networkCapabilities = cm.activeNetwork ?: return false
            Debug.i("[Network] cm.getNetworkCapabilities=${cm.getNetworkCapabilities(networkCapabilities)}")
            val actNw = cm.getNetworkCapabilities(networkCapabilities) ?: return false
            Debug.i("[Network] TRANSPORT_WIFI=${actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)}")
            Debug.i("[Network] TRANSPORT_CELLULAR=${actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)}")
            Debug.i("[Network] TRANSPORT_ETHERNET=${actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)}")
            Debug.i("[Network] TRANSPORT_WIFI_AWARE=${actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)}")
            Debug.i("[Network] TRANSPORT_VPN=${actNw.hasTransport(NetworkCapabilities.TRANSPORT_VPN)}")
            Debug.i("[Network] TRANSPORT_LOWPAN=${actNw.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN)}")
            Debug.i("[Network] TRANSPORT_BLUETOOTH=${actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)}")
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            cm.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }

        return result
    }
}