package com.jin.template.util.fragment

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.transition.Transition
import androidx.transition.TransitionSet

@Suppress("UNUSED")
class FragmentTransitionListener(private val fragment: Fragment) {
    private var mDoOnEndEnterAnimation: (() -> Unit)? = null
    private var isInit = true

    /** call at onCreateView() */
    fun doOnEndEnterAnimation(l: () -> Unit) {
        mDoOnEndEnterAnimation = l
    }

    /** call at onAttach() */
    fun onAttach() {
        var terminated = false
        (fragment.sharedElementEnterTransition as TransitionSet?)
            ?.addListener(object : Transition.TransitionListener {
                override fun onTransitionStart(transition: Transition) = Unit

                override fun onTransitionEnd(transition: Transition) {
                    if (!terminated) mDoOnEndEnterAnimation?.invoke()
                }

                override fun onTransitionCancel(transition: Transition) {
                    terminated = true
                }

                override fun onTransitionPause(transition: Transition) {
                    terminated = true
                }

                override fun onTransitionResume(transition: Transition) = Unit
            })
    }

    /** call at onCreateAnimation() */
    fun onCreateAnimation(enter: Boolean, nextAnim: Int): Animation? {
        if (nextAnim != 0x0) {
            val animation = AnimationUtils.loadAnimation(fragment.requireContext(), nextAnim)
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) = Unit

                override fun onAnimationEnd(animation: Animation?) {
                    if (isInit) {
                        isInit = false
                        if (enter) mDoOnEndEnterAnimation?.invoke()
                    }
                }

                override fun onAnimationRepeat(animation: Animation?) = Unit
            })
            return animation
        }
        return null
    }
}