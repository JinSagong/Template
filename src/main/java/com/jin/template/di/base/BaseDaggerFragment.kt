package com.jin.template.di.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.annotation.LayoutRes
import com.jin.template.fragment.FragmentTransitionUtil
import com.jin.template.fragment.FragmentUtil
import com.jin.template.util.Debug
import dagger.android.support.DaggerFragment

@Suppress("UNUSED")
abstract class BaseDaggerFragment : DaggerFragment() {
    @get: LayoutRes
    abstract val layoutId: Int
    lateinit var mView: View

    private val fragmentUtil by lazy { FragmentUtil(this) }
    val transitionUtil by lazy { FragmentTransitionUtil(this) }

    private var superOnBackPressed: (() -> Unit)? = null

    open fun onCreateView() = Unit
    open fun onEndEnterAnimation() = Unit
    open fun onBackPressed() = superOnBackPressed?.invoke() ?: Unit

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Debug.i("<Lifecycle> ${this::class.java.simpleName} [onAttach]")
        fragmentUtil.onAttach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Debug.i("<Lifecycle> ${this::class.java.simpleName} [onCreate]")
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        Debug.i("<Lifecycle> ${this::class.java.simpleName} [onCreateAnimation]")
        return fragmentUtil.onCreateAnimation(enter, nextAnim)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(layoutId, container, false).also { v ->
        Debug.i("<Lifecycle> ${this::class.java.simpleName} [onCreateView]")
        mView = v
        fragmentUtil.doOnEndEnterAnimation { onEndEnterAnimation() }
        fragmentUtil.doOnBackPressed {
            superOnBackPressed = it
            onBackPressed()
        }

        onCreateView()
    }

    override fun onStart() {
        super.onStart()
        Debug.i("<Lifecycle> ${this::class.java.simpleName} [onStart]")
    }

    override fun onResume() {
        super.onResume()
        Debug.i("<Lifecycle> ${this::class.java.simpleName} [onResume]")
    }

    override fun onPause() {
        super.onPause()
        Debug.i("<Lifecycle> ${this::class.java.simpleName} [onPause]")
    }

    override fun onStop() {
        super.onStop()
        Debug.i("<Lifecycle> ${this::class.java.simpleName} [onStop]")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Debug.i("<Lifecycle> ${this::class.java.simpleName} [onDestroyView]")
    }

    override fun onDestroy() {
        super.onDestroy()
        Debug.i("<Lifecycle> ${this::class.java.simpleName} [onDestroy]")
    }

    override fun onDetach() {
        super.onDetach()
        Debug.i("<Lifecycle> ${this::class.java.simpleName} [onDetach]")
    }
}