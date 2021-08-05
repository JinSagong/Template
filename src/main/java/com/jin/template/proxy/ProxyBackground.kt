package com.jin.template.proxy

import android.content.Context
import android.content.Intent

object ProxyBackground {
    fun startService(context: Context, intent: Intent) {
        ProxyActivity.startInBackground(context) {
            context.startService(intent)
        }
    }
}