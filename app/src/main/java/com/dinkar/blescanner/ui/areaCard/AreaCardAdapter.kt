package com.dinkar.blescanner.ui.areaCard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dinkar.blescanner.R
import com.dinkar.blescanner.data.DtArea

class AreaCardAdapter(private val context: Context) :
    ListAdapter<DtArea, AreaCardAdapter.ViewHolder>(WordsComparator()) {

   // private val courseModelArrayList: ArrayList<CourseModel>

    var previousSelIndex = -1
    var nowSelIndex = -1
    private var isLoading = false
    var selIndex = -1
    var isTeacherData = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // to inflate the layout for each item of recycler view.
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.card_area_layout, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // to set data to textview and imageview of each card layout
        val model: DtArea = getItem(position)
        holder.tvTitle.text = model.areaname

        if (isTeacherData) {
            // 教师数据收集
            if (selIndex == position) {
                if (isLoading) {
                    isLoading = false
                    holder.bg.setBackgroundColor(Color.WHITE)
                    holder.btReload.isVisible = true
                    selIndex = -1
                } else {
                    holder.bg.setBackgroundColor(ContextCompat.getColor(context,R.color.home_wifi))
                    holder.tvTitle.text = "取得中"
                    isLoading = true
                }
            }
        } else {
            // 测试数据收集
            if (previousSelIndex == position) {
                holder.bg.setBackgroundColor(Color.WHITE)
                holder.btReload.isVisible = true
                previousSelIndex = nowSelIndex
            }

            if (nowSelIndex == position) {
                holder.bg.setBackgroundColor(ContextCompat.getColor(context,R.color.home_wifi))
                holder.tvTitle.text = "取得中"
                holder.btReload.isVisible = false
            }
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
            }
        }
    }

//    override fun getItemCount(): Int {
//        // this method is used for showing number of card items in recycler view.
//        return courseModelArrayList.size
//    }

    // View holder class for initializing of your views such as TextView and Imageview.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val bg: RelativeLayout
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
        //this.courseModelArrayList = courseModelArrayList
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

    class WordsComparator : DiffUtil.ItemCallback<DtArea>() {
        override fun areItemsTheSame(oldItem: DtArea, newItem: DtArea): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: DtArea, newItem: DtArea): Boolean {
            return oldItem.areaname == newItem.areaname
        }
    }

}