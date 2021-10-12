package com.jin.template.util

import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.transition.TransitionInflater
import com.jin.template.R
import java.util.*

@Suppress("UNUSED")
class FragmentUtil(private val fa: FragmentActivity) {
    private var containerId = R.id.fcv_template
    private var hasBackStack = true
    private var enterAnimation = 0
    private var exitAnimation = 0
    private var popEnterAnimation = 0
    private var popExitAnimation = 0
    private val sharedAnimationList: Queue<Pair<View, String>> = LinkedList()

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

    fun setEnterAnimation(animation: ANIMATION) =
        apply { enterAnimation = animation.animRes }

    fun setExitAnimation(animation: ANIMATION) =
        apply { exitAnimation = animation.animRes }

    fun setPopEnterAnimation(animation: ANIMATION) =
        apply { popEnterAnimation = animation.animRes }

    fun setPopExitAnimation(animation: ANIMATION) =
        apply { popExitAnimation = animation.animRes }

    fun addSharedElement(view: View, name: String) =
        apply { sharedAnimationList.add(Pair(view, name)) }

    fun addFragment(fragment: Fragment, currentFragment: Fragment? = null) {
        val transaction = fa.supportFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)

        if (sharedAnimationList.isEmpty()) {
            transaction.setCustomAnimations(
                enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation
            )
        } else {
            do {
                val pop = sharedAnimationList.poll()
                    ?.also { transaction.addSharedElement(it.first, it.second) }
            } while (pop != null)
            fragment.sharedElementEnterTransition =
                TransitionInflater.from(fa).inflateTransition(android.R.transition.move)
            fragment.sharedElementReturnTransition =
                TransitionInflater.from(fa).inflateTransition(android.R.transition.move)
        }

        transaction.add(containerId, fragment)
        if (currentFragment != null) transaction.hide(currentFragment)
        if (hasBackStack) transaction.addToBackStack(null)
        transaction.commit()
    }

    fun resetAnimation() {
        enterAnimation = 0
        exitAnimation = 0
        popEnterAnimation = 0
        popExitAnimation = 0
        sharedAnimationList.clear()
    }

    fun onBackPress() {
        fa.supportFragmentManager.popBackStack()
    }

    enum class ANIMATION(@AnimRes val animRes: Int) {
        FADE_IN(R.anim.fade_in),
        FADE_OUT(R.anim.fade_out),
        SLIDE_IN_END(R.anim.slide_in_end),
        SLIDE_OUT_END(R.anim.slide_out_end),
        SLIDE_IN_BOTTOM(R.anim.slide_in_bottom),
        SLIDE_OUT_BOTTOM(R.anim.slide_out_bottom)
    }
}