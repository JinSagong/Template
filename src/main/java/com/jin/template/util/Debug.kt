package com.jin.template.util

import android.util.Log

@Suppress("UNUSED")
object Debug {
    fun i(msg: String) = Log.i("androidJ", msg)
    fun e(msg: String) = Log.e("androidJ", msg)

    fun request(msg: String) = Log.d("apiJ", msg)
    fun response(msg: String) = Log.v("apiJ", msg)
    fun error(t: Throwable) = Log.e("apiJ", t.message.orEmpty())
}