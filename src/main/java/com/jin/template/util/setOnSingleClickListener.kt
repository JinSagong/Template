package com.jin.template.util

import android.os.SystemClock
import android.view.View

private const val notClickableDuration = 600L
private var mLastClickTime = 0L

fun View.setOnSingleClickListener(l: ((View) -> Unit)?) {
    if (l == null) setOnClickListener(l)
    else setOnClickListener { view ->
        if (SystemClock.elapsedRealtime() - mLastClickTime < notClickableDuration) return@setOnClickListener
        mLastClickTime = SystemClock.elapsedRealtime()
        l.invoke(view)
    }
}

fun View.setOnSingleClickListener(l: View.OnClickListener?) {
    if (l == null) setOnClickListener(l)
    else setOnClickListener { view ->
        if (SystemClock.elapsedRealtime() - mLastClickTime < notClickableDuration) return@setOnClickListener
        mLastClickTime = SystemClock.elapsedRealtime()
        l.onClick(view)
    }
}