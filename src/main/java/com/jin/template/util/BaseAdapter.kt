package com.jin.template.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNUSED")
abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseAdapter<T>.BaseViewHolder>() {
    companion object {
        const val defaultViewType = 0
    }

    val itemList = arrayListOf<Pair<T, Int>>()

    abstract inner class BaseViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        abstract fun onBind(item: T, position: Int)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) =
        holder.onBind(itemList[position].first, position)

    override fun getItemViewType(position: Int) = itemList[position].second

    fun update(list: List<T>) = updateWithSingleViewType(list, defaultViewType)

    fun updateWithSingleViewType(list: List<T>, viewType: Int) =
        updateWithViewType(list.map { Pair(it, viewType) })

    fun updateWithViewType(list: List<Pair<T, Int>>) {
        itemList.clear()
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    fun updateAddItem(item: T, type: Int = defaultViewType) {
        itemList.add(Pair(item, type))
        notifyItemInserted(itemCount)
        notifyItemRangeInserted(itemCount, 1)
    }

    fun updateAddList(list: List<T>) = updateAddListWithSingleViewType(list, defaultViewType)

    fun updateAddListWithSingleViewType(list: List<T>, viewType: Int) =
        updateAddListWithViewType(list.map { Pair(it, viewType) })

    fun updateAddListWithViewType(list: List<Pair<T, Int>>) {
        if (list.isEmpty()) return
        val s = itemList.size
        itemList.addAll(list)
        notifyItemRangeInserted(s + 1, list.size)
    }

    fun clear() {
        itemList.clear()
        notifyDataSetChanged()
    }
}