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

    fun cancel() = try {
        toast?.cancel()
    } catch (e: Exception) {
        Debug.e("Toasty error: ${e.message}")
    }

    fun showAlert(@StringRes msgId: Int) = try {
        val msg = appContext.createConfigurationContext(
            Configuration(
                appContext.resources.configuration.apply { setLocale(Locale(defaultLanguage)) }
            )
        ).resources.getString(msgId)
        toast?.cancel()
        toast = alertToast(msg)
        toast!!.show()
    } catch (e: Exception) {
        Debug.e("Toasty error: ${e.message}")
    }

    fun showAlert(msg: String) = try  {
        toast?.cancel()
        toast = alertToast(msg)
        toast!!.show()
    } catch (e: Exception) {
        Debug.e("Toasty error: ${e.message}")
    }

    fun show(@StringRes msgId: Int) = try  {
        val msg = appContext.createConfigurationContext(
            Configuration(
                appContext.resources.configuration.apply { setLocale(Locale(defaultLanguage)) }
            )
        ).resources.getString(msgId)
        toast?.cancel()
        toast = messageToast(msg)
        toast!!.show()
    } catch (e: Exception) {
        Debug.e("Toasty error: ${e.message}")
    }

    fun show(msg: String) = try  {
        toast?.cancel()
        toast = messageToast(msg)
        toast!!.show()
    } catch (e: Exception) {
        Debug.e("Toasty error: ${e.message}")
    }
}