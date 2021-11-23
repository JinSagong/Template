package com.jin.template.di.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.jin.template.util.DayNightUtil
import com.jin.template.util.LocaleLanguageWrapper
import com.jin.template.util.StatusBar
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseDaggerActivity<B : ViewBinding>(private val bindingFactory: (LayoutInflater) -> B) :
    DaggerAppCompatActivity() {
    private var _binding: B? = null
    val binding: B get() = _binding!!

    private val dayNightUtil = DayNightUtil()
    open fun doOnDayMode() = StatusBar.setWindowLightStatusBar(this, true)
    open fun doOnNightMode() = StatusBar.setWindowLightStatusBar(this, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
        dayNightUtil.doOnDayMode(this::doOnDayMode)
        dayNightUtil.doOnNightMode(this::doOnNightMode)
        binding.root.post { dayNightUtil.setDayNightMode(resources.configuration) }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /**
     * should add below code
     *
     * AndroidManifest.xml
     * <activity android:configChanges="uiMode"/>
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        dayNightUtil.setDayNightMode(newConfig)
    }

    override fun attachBaseContext(newBase: Context?) =
        super.attachBaseContext(LocaleLanguageWrapper.wrap(newBase))
}