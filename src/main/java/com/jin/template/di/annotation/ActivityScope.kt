package com.jin.template.di.annotation

import javax.inject.Scope

@Target(
    AnnotationTarget.TYPE,
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION
)
@Scope
@Retention
@Suppress("UNUSED")
annotation class ActivityScope