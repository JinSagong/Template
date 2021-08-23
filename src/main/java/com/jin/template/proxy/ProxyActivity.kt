package com.jin.template.proxy

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import splitties.activities.start

internal class ProxyActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFinishOnTouchOutside(false)
        Handler(Looper.getMainLooper()).post {
            mDoInBackground?.invoke(this)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mDoInBackground = null
    }

    companion object {
        private var mDoInBackground: ((Context) -> Unit)? = null
        fun startInBackground(context: Context, doInBackground: (Context) -> Unit) {
            mDoInBackground = doInBackground
            context.start<ProxyActivity> {
                flags = Intent.FLAG_ACTIVITY_NO_ANIMATION or
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
        }
    }
}