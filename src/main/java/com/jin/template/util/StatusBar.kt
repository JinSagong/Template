package com.jin.template.util

import android.app.Activity
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment

@Suppress("UNUSED")
object StatusBar {
    private val typedValue by lazy { TypedValue() }

    fun setStatusBarColor(act: Activity, colorId: Int, isAttrId: Boolean = false) {
        if (isAttrId) {
            act.theme.resolveAttribute(colorId, typedValue, true)
            act.window.statusBarColor = ContextCompat.getColor(act, typedValue.resourceId)
        } else {
            act.window.statusBarColor = ContextCompat.getColor(act, colorId)
        }
    }

    @Suppress("DEPRECATION")
    fun setWindowLightStatusBar(act: Activity, enable: Boolean) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)  {
                act.window.decorView.systemUiVisibility =
                    if (enable) act.window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else act.window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
    }

    @Suppress("DEPRECATION")
    fun setTransparentStatusBar(act: Activity) {
        act.window.decorView.systemUiVisibility =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
        act.window.statusBarColor = Color.TRANSPARENT
        val bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        val win = act.window
        val winParams = win.attributes
        winParams.flags = winParams.flags and bits.inv()
        win.attributes = winParams
    }

    fun setHeightOfStatusBar(act: Activity, statusBar: View) {
        statusBar.viewTreeObserver.addOnPreDrawListener(object :
            ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                val rect = Rect()
                act.window.decorView.getWindowVisibleDisplayFrame(rect)
                val height = if (rect.top > 200) 0 else rect.top
                statusBar.updateLayoutParams<LinearLayout.LayoutParams> { this.height = height }
                statusBar.viewTreeObserver.removeOnPreDrawListener(this)
                return true
            }
        })
    }

    fun setHeightOfStatusBar(
        fragment: Fragment,
        statusBar: View,
        postponeAnimation: Boolean = false
    ) {
        if (postponeAnimation) fragment.postponeEnterTransition()
        statusBar.viewTreeObserver.addOnPreDrawListener(object :
            ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                val rect = Rect()
                fragment.requireActivity().window.decorView.getWindowVisibleDisplayFrame(rect)
                val height = if (rect.top > 200) 0 else rect.top
                statusBar.updateLayoutParams<LinearLayout.LayoutParams> { this.height = height }
                if (postponeAnimation) fragment.startPostponedEnterTransition()
                statusBar.viewTreeObserver.removeOnPreDrawListener(this)
                return true
            }
        })
    }
}