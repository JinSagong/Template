package com.jin.template.di.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.jin.template.util.LocaleLanguageWrapper
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseDaggerActivity<B : ViewBinding>(private val bindingFactory: (LayoutInflater) -> B) :
    DaggerAppCompatActivity() {
    private var _binding: B? = null
    val binding: B get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun attachBaseContext(newBase: Context?) =
        super.attachBaseContext(LocaleLanguageWrapper.wrap(newBase))
}