package com.jin.template.util

import android.util.Log

@Suppress("UNUSED")
object Debug {
    var showLog = true

    fun i(msg: String) = if (showLog) Log.i("androidJ", msg) else -1
    fun e(msg: String) = if (showLog) Log.e("androidJ", msg) else -1

    fun request(msg: String) = if (showLog) Log.d("apiJ", msg) else -1
    fun response(msg: String) = if (showLog) Log.v("apiJ", msg) else -1
    fun error(t: Throwable) = if (showLog) Log.e("apiJ", t.message.orEmpty()) else -1
}