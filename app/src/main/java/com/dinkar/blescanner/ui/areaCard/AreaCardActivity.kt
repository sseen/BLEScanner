package com.dinkar.blescanner.ui.areaCard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dinkar.blescanner.R

class AreaCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_area_card)

        val courseRV = findViewById<RecyclerView>(R.id.idRVArea)

        // Here, we have created new array list and added data to it
        val courseModelArrayList: ArrayList<CourseModel> = ArrayList<CourseModel>()
        courseModelArrayList.add(CourseModel("living room 1", 4, 1))
        courseModelArrayList.add(CourseModel("bed room 2", 3, 1))
        courseModelArrayList.add(CourseModel("kitchen 3", 4, 1))

        // we are initializing our adapter class and passing our arraylist to it.
        val courseAdapter = AreaCardAdapter(this, courseModelArrayList)

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        courseRV.layoutManager = linearLayoutManager
        courseRV.adapter = courseAdapter

    }
}