package com.jin.template.util

import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.jin.template.R

class FragmentUtil(private val fa: FragmentActivity) {
    private var containerId = R.id.fcv_template
    private var hasBackStack = true
    private var enterAnimation = 0
    private var exitAnimation = 0
    private var popEnterAnimation = 0
    private var popExitAnimation = 0

    fun setContainerId(@IdRes containerId: Int) = apply { this.containerId = containerId }

    fun setHasBackStack(hasBackStack: Boolean) = apply { this.hasBackStack = hasBackStack }

    fun setEnterAnimation(@AnimatorRes @AnimRes animation: Int) =
        apply { enterAnimation = animation }

    fun setExitAnimation(@AnimatorRes @AnimRes animation: Int) =
        apply { exitAnimation = animation }

    fun setPopEnterAnimation(@AnimatorRes @AnimRes animation: Int) =
        apply { popEnterAnimation = animation }

    fun setPopExitAnimation(@AnimatorRes @AnimRes animation: Int) =
        apply { popExitAnimation = animation }

    fun addFragment(fragment: Fragment) {
        val transaction = fa.supportFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        transaction.setCustomAnimations(
            enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation
        )
        transaction.add(containerId, fragment)
        if (hasBackStack) transaction.addToBackStack(null)
        transaction.commit()
    }

    fun onBackPress() {
        fa.supportFragmentManager.popBackStack()
    }
}