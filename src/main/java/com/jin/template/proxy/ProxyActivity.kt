package com.jin.template.proxy

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import splitties.activities.start

internal class ProxyActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFinishOnTouchOutside(false)
        mDoInBackground?.invoke()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDoInBackground = null
    }

    companion object {
        private var mDoInBackground: (() -> Unit)? = null
        fun startInBackground(context: Context, doInBackground: () -> Unit) {
            mDoInBackground = doInBackground
            context.start<ProxyActivity> {
                flags = Intent.FLAG_ACTIVITY_NO_ANIMATION or
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
        }
    }
}