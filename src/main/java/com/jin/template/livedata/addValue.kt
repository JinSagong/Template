package com.jin.template.livedata

@Suppress("UNUSED")
fun <T> LiveData<List<T>>.addValue(list: List<T>) =
    postValue(data.orEmpty().toCollection(ArrayList()).apply { addAll(list) })