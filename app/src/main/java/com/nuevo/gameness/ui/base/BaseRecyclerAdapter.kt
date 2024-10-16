package com.nuevo.gameness.ui.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseRecyclerAdapter<T, VB: ViewBinding>(
    private var itemList: List<T> = listOf()
): RecyclerView.Adapter<BaseRecyclerAdapter<T, VB>.ViewHolder>() {

    var onItemClickListener: ((pos: Int, item: T) -> Unit)? = null
    lateinit var context: Context

    abstract fun getViewBinding(inflater: LayoutInflater, parent: ViewGroup): VB
    abstract fun bind(position: Int, item: T, binding: VB)

    inner class ViewHolder(val binding: VB) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(getViewBinding(LayoutInflater.from(context), parent))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        bind(position, itemList[position], holder.binding)

    override fun getItemCount(): Int = itemList.size

    open fun updateAllData(newList: List<T>) {
        itemList = newList
        notifyDataSetChanged()
    }
    open fun getItems() :List<T>?{
        return itemList
    }
    open fun getItemAtPosition(pos:Int) :T?{
        if(itemList.size-1>= pos){
           return itemList[pos]
        }
        return null
    }
}