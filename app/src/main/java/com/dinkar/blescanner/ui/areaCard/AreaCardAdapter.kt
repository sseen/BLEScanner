package com.dinkar.blescanner.ui.areaCard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dinkar.blescanner.R

class AreaCardAdapter(private val context: Context, courseModelArrayList: ArrayList<CourseModel>) :
    RecyclerView.Adapter<AreaCardAdapter.ViewHolder>() {
    private val courseModelArrayList: ArrayList<CourseModel>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaCardAdapter.ViewHolder {
        // to inflate the layout for each item of recycler view.
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.card_area_layout, parent, false)
        return AreaCardAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AreaCardAdapter.ViewHolder, position: Int) {
        // to set data to textview and imageview of each card layout
        val model: CourseModel = courseModelArrayList[position]
        holder.tvTitle.text = model.getCourse_name()
    }

    override fun getItemCount(): Int {
        // this method is used for showing number of card items in recycler view.
        return courseModelArrayList.size
    }

    // View holder class for initializing of your views such as TextView and Imageview.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

         val tvTitle: TextView
         val btReload: ImageButton
        init {
            tvTitle = itemView.findViewById(R.id.idRVAreaTitle)
            btReload = itemView.findViewById(R.id.idRVAreaBt)
        }
    }

    // Constructor
    init {
        this.courseModelArrayList = courseModelArrayList
    }
}