package com.jin.template.di.module

import com.jin.template.di.annotation.ActivityScope
import com.jin.template.di.annotation.FragmentScope
import com.jin.template.di.base.BaseDaggerActivity
import com.jin.template.di.base.BaseDaggerFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
@Suppress("UNUSED")
abstract class BaseModule {
    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun baseDaggerActivity(): BaseDaggerActivity

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun baseDaggerFragment(): BaseDaggerFragment
}