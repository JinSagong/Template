package com.jin.template.util

import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

@Suppress("UNUSED")
class NavigationDrawer(
    private val act: FragmentActivity,
    private val drawerLayout: DrawerLayout,
    private val container: FrameLayout,
    navigationDrawer: Fragment
) {
    val gravity get() = (container.layoutParams as DrawerLayout.LayoutParams).gravity
    val isOpened get() = drawerLayout.isDrawerOpen(gravity)
    var drawerListener: ((Boolean) -> Unit)? = null
    var drawerOffsetListener: ((Float) -> Unit)? = null
    private var hasTab = false
    private val drawerToggle = object : ActionBarDrawerToggle(act, drawerLayout, 0, 0) {
        override fun onDrawerClosed(drawerView: View) {
            super.onDrawerClosed(drawerView)
            if (!hasTab) drawerListener?.invoke(true)
            hasTab = false
        }

        override fun onDrawerOpened(drawerView: View) {
            super.onDrawerOpened(drawerView)
            if (!hasTab) drawerListener?.invoke(false)
            hasTab = false
        }

        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            super.onDrawerSlide(drawerView, slideOffset)
            drawerOffsetListener?.invoke(slideOffset)
        }
    }

    init {
        val transaction = act.supportFragmentManager.beginTransaction()
        transaction.replace(container.id, navigationDrawer, navigationDrawer.javaClass.simpleName)
        transaction.commitAllowingStateLoss()
        drawerLayout.addDrawerListener(drawerToggle)
    }

    fun controlDrawer() = controlDrawer(isOpened.not())

    fun controlDrawer(doOpen: Boolean) {
        hasTab = true
        drawerListener?.invoke(doOpen.not())
        if (doOpen) drawerLayout.openDrawer(gravity) else drawerLayout.closeDrawer(gravity)
    }

    fun setScrimTransparent() {
        drawerLayout.setScrimColor(Color.TRANSPARENT)
    }

    fun setSlidable(slidable: Boolean, isOpened: Boolean = false) =
        drawerLayout.setDrawerLockMode(
            when {
                slidable -> DrawerLayout.LOCK_MODE_UNLOCKED
                isOpened -> DrawerLayout.LOCK_MODE_LOCKED_OPEN
                else -> DrawerLayout.LOCK_MODE_LOCKED_CLOSED
            }
        )

    fun close() = drawerLayout.removeDrawerListener(drawerToggle)
}