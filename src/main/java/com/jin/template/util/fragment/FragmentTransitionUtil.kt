package com.jin.template.util.fragment

import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.transition.*
import com.jin.template.R
import java.util.*

@Suppress("UNUSED")
class FragmentTransitionUtil(private val fa: FragmentActivity) {
    private var containerId = R.id.fcv_template
    private var hasBackStack = true
    private var enterAnimation = 0
    private var exitAnimation = 0
    private var popEnterAnimation = 0
    private var popExitAnimation = 0
    private val sharedAnimationList: Queue<Pair<View, String>> = LinkedList()

    fun setContainerId(@IdRes containerId: Int) = apply { this.containerId = containerId }

    fun setBackgroundColor(
        @ColorRes colorId: Int,
        view: View = fa.findViewById(R.id.cl_template_background)
    ) = apply { view.setBackgroundColor(ContextCompat.getColor(fa, colorId)) }

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

    fun addFragment(
        fragment: Fragment,
        tag: String = fragment::class.java.simpleName,
        startAndEndRadius: Pair<Int, Int>? = null,
        currentFragment: Fragment? = null
    ) {
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
            val transitionSet = TransitionSet().apply {
                addTransition(AutoTransition())
                addTransition(ChangeImageTransform())
                addTransition(ChangeBounds())
                addTransition(ChangeTransform())
                if (startAndEndRadius != null) addTransition(
                    ChangeOutlineRadiusTransition(startAndEndRadius.first, startAndEndRadius.second)
                )
            }
            fragment.sharedElementEnterTransition = transitionSet
            fragment.sharedElementReturnTransition = transitionSet
        }

//        val mFragment = fa.supportFragmentManager.findFragmentByTag(tag)
//        if (singletonMode && mFragment != null) transaction.detach(mFragment)
//        transaction.add(containerId, fragment, if (singletonMode) tag else null)
        transaction.add(containerId, fragment)
        if (currentFragment != null) transaction.hide(currentFragment)
        if (hasBackStack) transaction.addToBackStack(tag)
        transaction.commit()
    }

    fun resetAnimation() {
        enterAnimation = 0
        exitAnimation = 0
        popEnterAnimation = 0
        popExitAnimation = 0
        sharedAnimationList.clear()
    }

    fun checkBackStack(name: String): Boolean {
        (0 until fa.supportFragmentManager.backStackEntryCount).forEach {
            if (fa.supportFragmentManager.getBackStackEntryAt(it).name == name) return true
        }
        return false
    }

    fun popBackStack(name: String? = null, flag: Int = 0) {
        fa.supportFragmentManager.popBackStack(name, flag)
    }

    fun clearBackStack() {
        fa.supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    enum class ANIMATION(@AnimRes val animRes: Int) {
        FADE_IN(R.anim.fade_in),
        FADE_OUT(R.anim.fade_out),
        FADE_IN_START(R.anim.fade_in_start),
        FADE_OUT_START(R.anim.fade_out_start),
        FADE_IN_TOP(R.anim.fade_in_top),
        FADE_OUT_TOP(R.anim.fade_out_top),
        SLIDE_IN_END(R.anim.slide_in_end),
        SLIDE_OUT_END(R.anim.slide_out_end),
        SLIDE_IN_BOTTOM(R.anim.slide_in_bottom),
        SLIDE_OUT_BOTTOM(R.anim.slide_out_bottom)
    }
}