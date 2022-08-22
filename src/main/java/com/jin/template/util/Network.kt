package com.jin.template.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
//        Debug.i("[Network] cm.activityNetwork=${cm.activeNetwork}") // catch
        val networkCapabilities = cm.activeNetwork ?: return false
//        Debug.i(
//            "[Network] cm.getNetworkCapabilities=${cm.getNetworkCapabilities(networkCapabilities)}"
//        )
        val actNw = cm.getNetworkCapabilities(networkCapabilities) ?: return false
//        Debug.i("[Network] TRANSPORT_WIFI=${actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)}")
//        Debug.i("[Network] TRANSPORT_CELLULAR=${actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)}")
//        Debug.i("[Network] TRANSPORT_ETHERNET=${actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)}")
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//            Debug.i("[Network] TRANSPORT_WIFI_AWARE=${actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)}")
//        Debug.i("[Network] TRANSPORT_VPN=${actNw.hasTransport(NetworkCapabilities.TRANSPORT_VPN)}")
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1)
//            Debug.i("[Network] TRANSPORT_LOWPAN=${actNw.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN)}")
//        Debug.i("[Network] TRANSPORT_BLUETOOTH=${actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)}")
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    fun getNetworkType(): NetworkType {
        val networkCapabilities = cm.activeNetwork ?: return NetworkType.NONE
        val actNw = cm.getNetworkCapabilities(networkCapabilities) ?: return NetworkType.NONE
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.CELLULAR
            else -> NetworkType.NONE
        }
    }

    fun getUploadKbps(): Int {
        val networkCapabilities = cm.activeNetwork ?: return 0
        val actNw = cm.getNetworkCapabilities(networkCapabilities) ?: return 0
        Debug.i("[Network] uploadKbps=${actNw.linkUpstreamBandwidthKbps}")
        return actNw.linkUpstreamBandwidthKbps
    }

    fun getDownloadKbps(): Int {
        val networkCapabilities = cm.activeNetwork ?: return 0
        val actNw = cm.getNetworkCapabilities(networkCapabilities) ?: return 0
        Debug.i("[Network] downloadKbps=${actNw.linkDownstreamBandwidthKbps}")
        return actNw.linkDownstreamBandwidthKbps
    }

    enum class NetworkType {
        WIFI,
        CELLULAR,
        NONE
    }
}