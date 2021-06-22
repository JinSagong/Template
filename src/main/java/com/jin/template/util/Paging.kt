package com.jin.template.util

import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNUSED")
class Paging<T> private constructor(
    private val recyclerView: RecyclerView? = null,
    private val nestedScrollView: NestedScrollView? = null
) {
    private var adapter: BaseAdapter<T>? = null
    private var viewType: Int = BaseAdapter.defaultViewType
    private var onLoadListener: ((Int, Int, (List<T>) -> Unit) -> Unit)? = null
    private var onLoadAfterListener: ((Int) -> Unit)? = null

    private var capacity = 10

    private var page = 1
    private var pageWorking = 0

    fun setCapacity(capacity: Int = 10) = apply { this.capacity = capacity }
    fun setAdapter(adapter: BaseAdapter<T>) = apply { this.adapter = adapter }
    fun setViewType(viewType: Int) = apply { this.viewType = viewType }

    fun setDoOnLoad(l: (Int, Int, (List<T>) -> Unit) -> Unit) = apply { onLoadListener = l }
    fun setDoOnLoadAfter(l: (Int) -> Unit) = apply { onLoadAfterListener = l }

    fun startObserve() {
        page = 1
        pageWorking = 0

        if (recyclerView != null) initRecyclerView()
        if (nestedScrollView != null) initNestedScrollView()
    }

    fun stopObserve() {
        recyclerView?.removeOnScrollListener(recyclerViewListener)
        nestedScrollView?.setOnScrollChangeListener(null as NestedScrollView.OnScrollChangeListener?)
    }

    private fun initRecyclerView() {
        onLoadListener?.invoke(page, capacity) {
            if (page == 1) {
                adapter?.clear()
                recyclerView?.removeOnScrollListener(recyclerViewListener)
            }
            if (it.size < capacity) {
                if (it.isNotEmpty()) adapter?.updateAddListWithSingleViewType(it, viewType)
                page = -1
            } else {
                adapter?.updateAddListWithSingleViewType(it, viewType)
                page++
                recyclerView?.post {
                    if (recyclerView.canScrollHorizontally(1) ||
                        recyclerView.canScrollVertically(1)
                    ) {
                        recyclerView.addOnScrollListener(recyclerViewListener)
                    } else {
                        initRecyclerView()
                    }
                }
            }
            recyclerView?.post { onLoadAfterListener?.invoke(adapter?.itemCount ?: 0) }
        }
    }

    private fun initNestedScrollView() {
        onLoadListener?.invoke(page, capacity) {
            if (page == 1) {
                adapter?.clear()
                nestedScrollView?.setOnScrollChangeListener(null as NestedScrollView.OnScrollChangeListener?)
            }
            if (it.size < capacity) {
                if (it.isNotEmpty()) adapter?.updateAddListWithSingleViewType(it, viewType)
                page = -1
            } else {
                adapter?.updateAddListWithSingleViewType(it, viewType)
                page++
                nestedScrollView?.post {
                    if (nestedScrollView.canScrollHorizontally(1) ||
                        nestedScrollView.canScrollVertically(1)
                    ) {
                        nestedScrollView.setOnScrollChangeListener(nestedScrollViewListener)
                    } else {
                        initRecyclerView()
                    }
                }
            }
            nestedScrollView?.post { onLoadAfterListener?.invoke(adapter?.itemCount ?: 0) }
        }
    }

    private val recyclerViewListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if ((dx != 0 && !recyclerView.canScrollHorizontally(1)) ||
                    (dy != 0 && !recyclerView.canScrollVertically(1))
                ) {
                    if (pageWorking < page) {
                        pageWorking = page
                        onLoadListener?.invoke(page, capacity) {
                            if (it.size < capacity) {
                                if (it.isNotEmpty())
                                    adapter?.updateAddListWithSingleViewType(it, viewType)
                                page = -1
                            } else {
                                adapter?.updateAddListWithSingleViewType(it, viewType)
                                page++
                            }
                            recyclerView.post {
                                onLoadAfterListener?.invoke(adapter?.itemCount ?: 0)
                            }
                        }
                    }
                }
            }
        }
    }

    private val nestedScrollViewListener by lazy {
        NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldX, oldY ->
            if ((scrollX != oldX && !v.canScrollHorizontally(1)) ||
                (scrollY != oldY && !v.canScrollVertically(1))
            ) {
                if (pageWorking < page) {
                    pageWorking = page
                    onLoadListener?.invoke(page, capacity) {
                        if (it.size < capacity) {
                            if (it.isNotEmpty())
                                adapter?.updateAddListWithSingleViewType(it, viewType)
                            page = -1
                        } else {
                            adapter?.updateAddListWithSingleViewType(it, viewType)
                            page++
                        }
                        v.post { onLoadAfterListener?.invoke(adapter?.itemCount ?: 0) }
                    }
                }
            }
        }
    }

    companion object {
        fun <T> with(recyclerView: RecyclerView) =
            Paging<T>(recyclerView = recyclerView)

        fun <T> with(nestedScrollView: NestedScrollView) =
            Paging<T>(nestedScrollView = nestedScrollView)
    }
}