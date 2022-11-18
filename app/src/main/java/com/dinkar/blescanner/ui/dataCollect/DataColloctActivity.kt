package com.dinkar.blescanner.ui.dataCollect

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import com.dinkar.blescanner.R
import com.dinkar.blescanner.ui.areaCard.AreaCardActivity
import com.dinkar.blescanner.ui.areaCard.AreaCardAdapter

class DataColloctActivity : AreaCardActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)

        // hide menu item button
        val item = menu?.findItem(R.id.save_item) as MenuItem
        item.isVisible = false
        courseAdapter.isTeacherData = false
        // disable long press delete item
        courseAdapter.setOnItemClickListener(object : AreaCardAdapter.OnItemClickListener {
            @SuppressLint("RestrictedApi")
            override fun onItemLongClick(view: View?, pos: Int) {
            }
        })
        // click cell selection
        courseAdapter.setOnItemShortClickListener(object : AreaCardAdapter.OnItemClickListener {
            override fun onItemLongClick(view: View?, pos: Int) {
                courseAdapter.previousSelIndex = courseAdapter.nowSelIndex
                courseAdapter.nowSelIndex = pos
                courseAdapter.notifyItemChanged(pos)
                courseAdapter.notifyItemChanged(courseAdapter.previousSelIndex)
            }
        })

        return true
    }
}