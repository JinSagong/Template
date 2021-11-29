package com.jin.template.di.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.viewbinding.ViewBinding
import com.jin.template.fragment.FragmentTransitionUtil
import com.jin.template.fragment.FragmentUtil
import com.jin.template.util.Debug
import dagger.android.support.DaggerFragment

@Suppress("UNUSED")
abstract class BaseDaggerFragment<B : ViewBinding>(private val bindingFactory: (LayoutInflater, ViewGroup?, Boolean) -> B) :
    DaggerFragment() {
    private var _binding: B? = null
    val binding get() = _binding!!

    private val fragmentUtil by lazy { FragmentUtil(this) }
    val transitionUtil by lazy { FragmentTransitionUtil(this) }

    private var superOnBackPressed: (() -> Unit)? = null

    open fun onCreateView() = Unit
    open fun onEndEnterAnimation() = Unit
    open fun onBackPressed() = superOnBackPressed?.invoke() ?: fragmentUtil.onBackPressed()

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
    ): View {
        Debug.i("<Lifecycle> ${this::class.java.simpleName} [onCreateView]")

        _binding = bindingFactory.invoke(inflater, container, false)
        fragmentUtil.doOnEndEnterAnimation { onEndEnterAnimation() }
        fragmentUtil.doOnBackPressed {
            superOnBackPressed = it
            onBackPressed()
        }
        onCreateView()

        return binding.root
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
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        Debug.i("<Lifecycle> ${this::class.java.simpleName} [onDetach]")
    }
}