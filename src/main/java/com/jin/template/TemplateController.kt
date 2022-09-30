package com.jin.template

import android.app.Application
import android.content.Context
import android.graphics.Typeface
import androidx.annotation.StringRes

object TemplateController {
    lateinit var appContext: Context
    lateinit var defaultLanguage: String
    lateinit var offlineMsg: String
    lateinit var confirmMsg: String
    lateinit var cancelMsg: String
    lateinit var fcmProjectId: String
    lateinit var fcmKey: String

    fun setApplication(app: Application) = apply { appContext = app.applicationContext }
    fun setDefaultLanguage(language: String) = apply { defaultLanguage = language }
    fun setOfflineMsg(msg: String) = apply { offlineMsg = msg }
    fun setOfflineMsg(@StringRes msgId: Int) = apply { offlineMsg = appContext.getString(msgId) }
    fun setConfirmMsg(msg: String) = apply { confirmMsg = msg }
    fun setConfirmMsg(@StringRes msgId: Int) = apply { confirmMsg = appContext.getString(msgId) }
    fun setCancelMsg(msg: String) = apply { cancelMsg = msg }
    fun setCancelMsg(@StringRes msgId: Int) = apply { cancelMsg = appContext.getString(msgId) }
    fun setFcmProjectId(id: String) = apply { fcmProjectId = id }
    fun setFcmKey(key: String) = apply { fcmKey = key }
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