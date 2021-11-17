package com.jin.template.di.base

import android.content.Context
import com.jin.template.util.LocaleLanguageWrapper
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseDaggerActivity: DaggerAppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) =
        super.attachBaseContext(LocaleLanguageWrapper.wrap(newBase))
}