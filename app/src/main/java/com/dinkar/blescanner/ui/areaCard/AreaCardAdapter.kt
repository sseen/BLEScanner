package com.dinkar.blescanner.ui.areaCard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.dinkar.blescanner.R

class AreaCardAdapter(private val context: Context, courseModelArrayList: ArrayList<CourseModel>) :
    RecyclerView.Adapter<AreaCardAdapter.ViewHolder>() {

    private val courseModelArrayList: ArrayList<CourseModel>

    var previousSelIndex = -1;
    var nowSelIndex = -1;
    var isLoading = -1;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaCardAdapter.ViewHolder {
        // to inflate the layout for each item of recycler view.
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.card_area_layout, parent, false)
        return AreaCardAdapter.ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: AreaCardAdapter.ViewHolder, position: Int) {
        // to set data to textview and imageview of each card layout
        val model: CourseModel = courseModelArrayList[position]
        holder.tvTitle.text = model.getCourse_name()

        if (previousSelIndex == position) {
            holder.bg.setBackgroundColor(Color.WHITE)
            holder.btReload.isVisible = true
            previousSelIndex = nowSelIndex;
        }

        if (nowSelIndex == position) {
            holder.bg.setBackgroundColor(R.color.colorPrimary);
            holder.tvTitle.text = "取得中"
            holder.btReload.isVisible = false
        }

        if (onItemClickListener != null) {
            holder.itemView.setOnLongClickListener {
                onItemClickListener!!.onItemLongClick(holder.itemView, position)
                false
            }
        }
        if (onItemShortClickListener != null) {
            holder.itemView.setOnClickListener {
                onItemShortClickListener!!.onItemLongClick(holder.itemView, position)
                false
            }
        }
    }

    override fun getItemCount(): Int {
        // this method is used for showing number of card items in recycler view.
        return courseModelArrayList.size
    }

    // View holder class for initializing of your views such as TextView and Imageview.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val bg: View
         val tvTitle: TextView
         val btReload: ImageButton
        init {
            tvTitle = itemView.findViewById(R.id.idRVAreaTitle)
            btReload = itemView.findViewById(R.id.idRVAreaBt)
            btReload.isVisible = false
            bg = itemView.findViewById(R.id.idRVContent)
        }
    }

    // Constructor
    init {
        this.courseModelArrayList = courseModelArrayList
    }

    // add long press listener
    private var onItemClickListener: OnItemClickListener? = null
    private var onItemShortClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemLongClick(view: View?, pos: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnItemShortClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemShortClickListener = onItemClickListener
    }
}