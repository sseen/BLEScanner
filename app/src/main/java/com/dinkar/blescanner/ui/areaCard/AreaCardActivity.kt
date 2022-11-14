package com.dinkar.blescanner.ui.areaCard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dinkar.blescanner.BaseDetailActivity
import com.dinkar.blescanner.R
import com.dinkar.blescanner.ui.areaCard.detail.AreaDetailActivity
import es.dmoral.toasty.Toasty


class AreaCardActivity : BaseDetailActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_area_card)
        setTitle(R.string.home_bt_location)

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


        courseAdapter.setOnItemClickListener(object : AreaCardAdapter.OnItemClickListener {
            @SuppressLint("RestrictedApi")
            override fun onItemLongClick(view: View?, pos: Int) {
                val popupMenu = view?.let { PopupMenu(this@AreaCardActivity, it) }
                if (popupMenu != null) {
                    popupMenu.menuInflater.inflate(R.menu.delete, popupMenu.menu)

                    //弹出式菜单的菜单项点击事件
                    popupMenu.setOnMenuItemClickListener(object :
                        PopupMenu.OnMenuItemClickListener {
                        override fun onMenuItemClick(item: MenuItem): Boolean {
//                            if (item.itemId == R.id.delete) {
//                                chatData.remove(pos)
//                                chatAdapter.notifyItemRemoved(pos)
//                            }
                            return false
                        }
                    })

                    var one = popupMenu::class.java.getDeclaredField("mPopup")
                    one.isAccessible = true
                    var mHelper = one.get(popupMenu) as MenuPopupHelper
                    mHelper.setForceShowIcon(true)

//                    val pWindow = PopupWindow(view).apply {
//                        isOutsideTouchable = true
//                        isFocusable = true
//                    }

//                    pWindow.show
                    popupMenu.show()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)

        menu?.findItem(R.id.save_item)?.setIcon(R.drawable.baseline_add_circle_white_24dp)

        return true
    }


    override fun othersOnOptionsItemSelected(item: MenuItem) {
        super.othersOnOptionsItemSelected(item)
        if (item.itemId == R.id.save_item) {
            val intent1 = Intent(this, AreaDetailActivity::class.java)
            startActivity(intent1)
        } else {
            Toasty.success(applicationContext, "Success!", Toast.LENGTH_SHORT, true).show();

            finish()
        }
    }
}