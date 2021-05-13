package com.jin.template.livedata

@Suppress("UNUSED")
data class DataLiveValue<T>(
    val value: T,
    val observable: Boolean = true
)