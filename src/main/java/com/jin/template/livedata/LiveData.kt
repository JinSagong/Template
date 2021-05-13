package com.jin.template.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.jin.template.util.Debug

@Suppress("UNUSED")
class LiveData<T> : MutableLiveData<DataLiveValue<T>>() {
    val data: T? get() = value?.value

    fun postValue(value: T, observable: Boolean = true) =
        postValue(DataLiveValue(value, observable))

    fun notifyValue() = value?.let { setValue(DataLiveValue(it.value, it.observable)) }

    fun observeData(
        owner: LifecycleOwner,
        doFirst: Boolean = false,
        allowNotObservable: Boolean = true,
        observer: (T) -> Unit
    ) {
        if (doFirst && value != null) observer(value!!.value)
        observe(owner, {
            Debug.i("[Observe] $it")
            if (value?.observable == true || allowNotObservable) observer(it.value)
        })
    }

    fun observeDataForever(
        observer: LiveDataObserver<T>,
        doFirst: Boolean = false
    ) {
        if (doFirst && value != null) observer.doFunction(value!!)
        observeForever(observer)
    }
}