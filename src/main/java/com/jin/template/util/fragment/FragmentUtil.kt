package com.jin.template.util.fragment

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.transition.Transition
import androidx.transition.TransitionSet
import com.jin.template.util.Debug

@Suppress("UNUSED")
class FragmentUtil(private val fragment: Fragment) {
    private var mDoOnEndEnterAnimation: (() -> Unit)? = null
    private var backPressedBlockCallback: OnBackPressedCallback? = null
    private var terminated = false
        set(value) {
            if (value) backPressedBlockCallback?.remove()
            field = value
        }

    /** call at onCreateView() */
    fun doOnEndEnterAnimation(l: () -> Unit) {
        mDoOnEndEnterAnimation = l
    }

    /** call at onCreateView() */
    fun doOnBackPressed(l: (() -> Unit) -> Unit) {
        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                l.invoke {
                    remove()
                    fragment.requireActivity().onBackPressed()
                }
            }
        }
        fragment.requireActivity()
            .onBackPressedDispatcher
            .addCallback(fragment, backPressedCallback)
    }

    /** call at onAttach() */
    fun blockBackPressWhileTransition() {
        backPressedBlockCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() = Unit
        }
        fragment.requireActivity()
            .onBackPressedDispatcher
            .addCallback(fragment, backPressedBlockCallback!!)
    }

    /** call at onAttach()
     *  Use this method using SharedElementTransition
     * */
    fun onAttach() {
        (fragment.sharedElementEnterTransition as TransitionSet?)
            ?.addListener(object : Transition.TransitionListener {
                override fun onTransitionStart(transition: Transition) = Unit

                override fun onTransitionEnd(transition: Transition) {
                    Debug.e("Transition[onTransitionEnd]")
                    if (!terminated) mDoOnEndEnterAnimation?.invoke()
                    terminated = true
                }

                override fun onTransitionCancel(transition: Transition) {
                    Debug.e("Transition[onTransitionCancel]")
                    terminated = true
                }

                override fun onTransitionPause(transition: Transition) {
                    Debug.e("Transition[onTransitionPause]")
                    terminated = true
                }

                override fun onTransitionResume(transition: Transition) = Unit
            })
    }

    /** call at onCreateAnimation()
     *  Use this method using Transition
     *  */
    fun onCreateAnimation(enter: Boolean, nextAnim: Int): Animation? {
        if (nextAnim != 0x0) {
            val animation = AnimationUtils.loadAnimation(fragment.requireContext(), nextAnim)
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) = Unit

                override fun onAnimationEnd(animation: Animation?) {
                    if (!terminated) {
                        terminated = true
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