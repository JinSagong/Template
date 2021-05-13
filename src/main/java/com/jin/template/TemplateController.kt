package com.jin.template

import android.app.Application
import android.content.Context
import android.graphics.Typeface
import androidx.annotation.StringRes

object TemplateController {
    lateinit var appContext: Context
    lateinit var defaultLanguage: String
    lateinit var offlineMsg: String

    fun setApplication(app: Application) = apply { appContext = app.applicationContext }
    fun setDefaultLanguage(language: String) = apply { defaultLanguage = language }
    fun setOfflineMsg(msg: String) = apply { offlineMsg = msg }
    fun setOfflineMsg(@StringRes msgId: Int) = apply { offlineMsg = appContext.getString(msgId) }
    fun setDefaultFont(staticTypefaceFieldName: String, fontAssetName: String) = apply {
        val regular = Typeface.createFromAsset(appContext.assets, fontAssetName)
        try {
            val staticField = Typeface::class.java.getDeclaredField(staticTypefaceFieldName)
            staticField.isAccessible = true
            staticField[null] = regular
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }
}