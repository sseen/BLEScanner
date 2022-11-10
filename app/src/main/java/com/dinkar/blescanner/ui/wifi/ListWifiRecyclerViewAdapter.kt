package com.dinkar.blescanner.ui.wifi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dinkar.blescanner.databinding.ListWifiViewHolderBinding

class ListWifiRecyclerViewAdapter: RecyclerView.Adapter<ListWifiViewHolder>() {
    val listTitles = arrayOf("Shopping List", "Chores", "Android Tutorials")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListWifiViewHolder {
        val binding = ListWifiViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListWifiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListWifiViewHolder, position: Int) {
        holder.binding.itemStr.text = listTitles[position]
    }

    override fun getItemCount(): Int {
        return listTitles.size
    }

}