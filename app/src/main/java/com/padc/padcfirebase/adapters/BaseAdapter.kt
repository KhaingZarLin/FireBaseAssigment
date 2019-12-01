package com.padc.padcfirebase.adapters

import androidx.recyclerview.widget.RecyclerView
import com.padc.padcfirebase.viewholders.BaseViewHolder

abstract class BaseAdapter <VH: BaseViewHolder<T>, T> :RecyclerView.Adapter<VH>() {

    private var dataList: MutableList<T> = ArrayList()

    override fun getItemCount(): Int  = dataList.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.data = dataList[position]
    }

    fun setNewData(newData: MutableList<T>){
        dataList = newData
        notifyDataSetChanged()
    }

    fun appendNewData(newData: List<T>){
        dataList.addAll(newData)
        notifyDataSetChanged()
    }




}