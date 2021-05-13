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

    fun updateWithViewType(list: List<Pair<T, Int>>) {
        itemList.clear()
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    fun updateWithSingleViewType(list: List<T>, viewType: Int) {
        itemList.clear()
        itemList.addAll(list.map { Pair(it, viewType) })
        notifyDataSetChanged()
    }

    fun updateAdd(list: List<T>, viewType: Int) {
        val s = itemList.size
        if (list.size > s) {
            itemList.addAll(list.subList(s, list.size).map { Pair(it, viewType) })
            notifyItemRangeInserted(s, list.size)
        } else {
//            itemList.clear()
//            itemList.addAll(list.map { Pair(it, viewType) })
//            notifyDataSetChanged()
        }
    }

    fun clear() {
        itemList.clear()
        notifyDataSetChanged()
    }
}