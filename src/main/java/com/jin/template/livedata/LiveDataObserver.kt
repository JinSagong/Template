package com.jin.template.livedata

import androidx.lifecycle.Observer
import com.jin.template.util.Debug

@Suppress("UNUSED")
class LiveDataObserver<T>(private val function: (T) -> Unit) : Observer<DataLiveValue<T>> {
    override fun onChanged(t: DataLiveValue<T>) {
        Debug.i("[ObserveForever] $t")
        if (t.observable) function.invoke(t.value)
    }

    fun doFunction(t: DataLiveValue<T>) = function.invoke(t.value)
}