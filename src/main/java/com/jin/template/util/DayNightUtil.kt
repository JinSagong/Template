package com.jin.template.util

import android.content.res.Configuration

@Suppress("UNUSED")
class DayNightUtil {
    private var onDayModeListener: (() -> Unit)? = null
    private var onNightModeListener: (() -> Unit)? = null
    fun doOnDayMode(l: (() -> Unit)?) = apply { onDayModeListener = l }
    fun doOnNightMode(l: (() -> Unit)?) = apply { onNightModeListener = l }

    fun setDayNightMode(configuration: Configuration) {
        when (configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> onDayModeListener?.invoke()
            Configuration.UI_MODE_NIGHT_YES -> onNightModeListener?.invoke()
        }
    }

    fun setDayMode() {
        onDayModeListener?.invoke()
    }

    fun setNightMode() {
        onNightModeListener?.invoke()
    }
}