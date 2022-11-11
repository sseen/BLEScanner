package com.dinkar.blescanner.ui.area

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dinkar.blescanner.databinding.ListAreaViewHolderBinding

class ListAreaRVAdapter:RecyclerView.Adapter<ListAreaViewHolder>() {

    val listTitles = arrayOf("Shopping List", "Chores", "Android Tutorials")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAreaViewHolder {
        val binding = ListAreaViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListAreaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListAreaViewHolder, position: Int) {
        holder.binding.itemName.text = listTitles[position]
    }

    override fun getItemCount(): Int {
        return listTitles.size
    }
}