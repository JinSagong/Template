package com.jin.template.util

import android.content.Context
import android.os.Build
import com.jin.template.TemplateController.defaultLanguage
import java.util.*

@Suppress("UNUSED")
object LocaleLanguageWrapper {
    fun wrap(base: Context?): Context? {
        val mLocale = Locale(defaultLanguage)
        val config = base?.resources?.configuration?.apply {
            // configuration.locale = mLocate 작업이 Deprecated 됨
            @Suppress("DEPRECATION")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) setLocale(mLocale)
            else locale = mLocale
        } ?: return base
        return base.createConfigurationContext(config)
    }
}