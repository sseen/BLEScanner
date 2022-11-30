package com.dinkar.blescanner.ui.dataCollect

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.dinkar.blescanner.R
import com.dinkar.blescanner.Utils
import com.dinkar.blescanner.ui.areaCard.AreaCardActivity
import com.dinkar.blescanner.ui.areaCard.AreaCardAdapter
import kotlinx.android.synthetic.main.activity_area_card.*

class DataColloctActivity : AreaCardActivity() {
    var isStarted = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        setTitle(R.string.home_bt_data)

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
                if (!isStarted) {
                    setUpBluetoothManager()
                    isStarted = true
                }
                courseAdapter.previousSelIndex = courseAdapter.nowSelIndex
                courseAdapter.nowSelIndex = pos
                courseAdapter.notifyItemChanged(pos)
                courseAdapter.notifyItemChanged(courseAdapter.previousSelIndex)
            }
        })

        // bottom buttons control
        idBt_DataCollect_finish.setOnClickListener {
            idBt_DataCollect_save.isEnabled = true
            courseAdapter.previousSelIndex = courseAdapter.nowSelIndex
            courseAdapter.nowSelIndex = -1
            courseAdapter.notifyItemChanged(courseAdapter.previousSelIndex)

            btScanner!!.stopScan(leScanCallback)
        }
        idBt_DataCollect_save.setOnClickListener {
            // mSimpleDialog.show()
            val intent1 = Intent(applicationContext, DataCollectDetailActivity::class.java)
            intent1.putExtra(Utils.kTearcherOrData, "data")
            startActivity(intent1)
        }

        return true
    }
}