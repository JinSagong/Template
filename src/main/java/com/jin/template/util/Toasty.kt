package com.jin.template.util

import android.content.res.Configuration
import android.widget.Toast
import androidx.annotation.StringRes
import com.jin.template.R
import com.jin.template.TemplateController.appContext
import com.jin.template.TemplateController.defaultLanguage
import com.muddzdev.styleabletoast.StyleableToast
import java.util.*

@Suppress("UNUSED")
object Toasty {
    private var toast: StyleableToast? = null

    private fun alertToast(msg: String) = StyleableToast.makeText(
        appContext, msg, Toast.LENGTH_SHORT, R.style.AlertToast
    )

    private fun messageToast(msg: String) = StyleableToast.makeText(
        appContext, msg, Toast.LENGTH_LONG, R.style.MsgToast
    )

    fun cancel() = toast?.cancel()

    fun showAlert(@StringRes msgId: Int) {
        val msg = appContext.createConfigurationContext(
            Configuration(
                appContext.resources.configuration.apply { setLocale(Locale(defaultLanguage)) }
            )
        ).resources.getString(msgId)
        toast?.cancel()
        toast = alertToast(msg)
        toast!!.show()
    }

    fun showAlert(msg: String) {
        toast?.cancel()
        toast = alertToast(msg)
        toast!!.show()
    }

    fun show(@StringRes msgId: Int) {
        val msg = appContext.createConfigurationContext(
            Configuration(
                appContext.resources.configuration.apply { setLocale(Locale(defaultLanguage)) }
            )
        ).resources.getString(msgId)
        toast?.cancel()
        toast = messageToast(msg)
        toast!!.show()
    }

    fun show(msg: String) {
        toast?.cancel()
        toast = messageToast(msg)
        toast!!.show()
    }
}