package com.jin.template.di.module

import androidx.lifecycle.ViewModelProvider
import com.jin.template.di.factory.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
@Suppress("UNUSED")
abstract class ViewModelFactoryModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}