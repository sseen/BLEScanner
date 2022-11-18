package com.dinkar.blescanner.ui.areaCard

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dinkar.blescanner.BaseDetailActivity
import com.dinkar.blescanner.R
import com.dinkar.blescanner.UserModel
import com.dinkar.blescanner.Utils
import com.dinkar.blescanner.ui.dataCollect.DataCollectDetailActivity
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import dev.shreyaspatil.MaterialDialog.MaterialDialog
import dev.shreyaspatil.MaterialDialog.model.TextAlignment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_area_card.*
import sh.tyy.wheelpicker.DayTimePicker
import sh.tyy.wheelpicker.core.TextWheelAdapter
import sh.tyy.wheelpicker.core.TextWheelPickerView


open class AreaCardActivity : BaseDetailActivity() {

    // Here, we have created new array list and added data to it
    val courseModelArrayList: ArrayList<CourseModel> = ArrayList()

    private var selRoomIdx:Int = 0
    private var selRoomNumberIdx:Int = 0
    private val roomNumberList = ('A'..'Z').map {
        TextWheelPickerView.Item(
            "$it",
            "$it"
        )
    }
    private val roomMap = mapOf(
        "bedroom" to "bedroom","hall" to "hall",
        "kitchen" to "kitchen","living" to "living",
        "toilet" to "toilet","wic" to "wic",
        "room_wa" to "room_wa","room_yo" to "room_yo",
        "stairs" to "stairs","other" to "other"
    )
    private val roomIndexList = listOf(
        "bedroom" , "hall",
        "kitchen" , "living",
        "toilet" , "wic",
        "room_wa" , "room_yo",
        "stairs" , "other"
    )
    private val roomList = roomIndexList.map {
        TextWheelPickerView.Item(
            it,
            it
        )
    }

    lateinit var courseAdapter:AreaCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_area_card)
        setTitle(R.string.home_bt_location)

        val mPrefs =  getSharedPreferences(Utils.share_pre, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = mPrefs.getString(Utils.user, "")
        val myUser = gson.fromJson(json, UserModel::class.java)

        if (myUser != null) {
            val lblTitle = findViewById<TextView>(R.id.idTVDeviceUser)
            val deviceName =
                getString(R.string.beacon_device_user, myUser.device,myUser.userName)
            lblTitle.text = deviceName
        }

        val courseRV = findViewById<RecyclerView>(R.id.idRVArea)

        courseModelArrayList.add(CourseModel("living room 1", 4, 1))
        courseModelArrayList.add(CourseModel("bed room 2", 3, 1))
        courseModelArrayList.add(CourseModel("kitchen 3", 4, 1))

        // we are initializing our adapter class and passing our arraylist to it.
        courseAdapter = AreaCardAdapter(this, courseModelArrayList)

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // in below two lines we are setting layout-manager and adapter to our recycler view.
        courseRV.layoutManager = linearLayoutManager
        courseRV.adapter = courseAdapter

        // Simple Material Dialog
        val mSimpleDialog = MaterialDialog.Builder(this)
            .setTitle("保存", TextAlignment.CENTER)
            .setMessage(Html.fromHtml(R.string.data_collect_title.toString()), TextAlignment.CENTER)
            .setCancelable(false)
            .setPositiveButton("保存") { dialogInterface, _ ->
                dialogInterface.dismiss()

                val intent1 = Intent(applicationContext, DataCollectDetailActivity::class.java)
                startActivity(intent1)
            }
            .setNegativeButton("キャンセル") { dialogInterface, _ ->
                dialogInterface.dismiss()
                finish()
            }
            .build()

        // buttons control
        idBt_DataCollect_finish.setOnClickListener {
            idBt_DataCollect_save.isEnabled = true
            courseAdapter.previousSelIndex = courseAdapter.nowSelIndex
            courseAdapter.nowSelIndex = -1
            courseAdapter.notifyItemChanged(courseAdapter.previousSelIndex)
        }
        idBt_DataCollect_save.setOnClickListener {
            // mSimpleDialog.show()
            val intent1 = Intent(applicationContext, DataCollectDetailActivity::class.java)
            startActivity(intent1)
        }

        courseAdapter.isTeacherData = true
        // click cell selection
        courseAdapter.setOnItemShortClickListener(object : AreaCardAdapter.OnItemClickListener {
            override fun onItemLongClick(view: View?, pos: Int) {
                if (courseAdapter.selIndex != -1) {
                    if (courseAdapter.selIndex == pos) {
                        courseAdapter.selIndex = pos
                        courseAdapter.notifyItemChanged(pos)
                    }
                } else {
                    courseAdapter.selIndex = pos
                    courseAdapter.notifyItemChanged(pos)
                }
            }
        })

        courseAdapter.setOnItemClickListener(object : AreaCardAdapter.OnItemClickListener {
            @SuppressLint("RestrictedApi")
            override fun onItemLongClick(view: View?, pos: Int) {
                val popupMenu = view?.let { PopupMenu(this@AreaCardActivity, it) }
                if (popupMenu != null) {
                    popupMenu.menuInflater.inflate(R.menu.delete, popupMenu.menu)

                    //弹出式菜单的菜单项点击事件
                    popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                        if (item.itemId == R.id.idItem_delete) {
                            courseModelArrayList.removeAt(pos)
                            courseAdapter.notifyItemRemoved(pos)
                        }
                        false
                    })

                    val one = popupMenu::class.java.getDeclaredField("mPopup")
                    one.isAccessible = true
                    val mHelper = one.get(popupMenu) as MenuPopupHelper
                    mHelper.setForceShowIcon(true)

                    // val pWindow = PopupWindow(view).apply {
                    //     isOutsideTouchable = true
                    //     isFocusable = true
                    // }
                    // pWindow.show
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
            // 直接输入detail
            // val intent1 = Intent(this, AreaDetailActivity::class.java)
            // 直接选择detail
            // val intent1 = Intent(this, AreaDetailSelectorActivity::class.java)
            // startActivity(intent1)
            // 都不用了，改为两排 picker
            showPicker()
        } else {
            Toasty.success(applicationContext, "Success!", Toast.LENGTH_SHORT, true).show()

            finish()
        }
    }

    private fun showPicker() {
        val picker = DayTimePicker(this)

        picker.show(window)
        picker.pickerView?.day = 0
        picker.pickerView?.hour = 0

        // reflect to change data source
        val one = picker.pickerView!!::class.java.getDeclaredField("minutePickerView")
        one.isAccessible = true
        val one2 = one.get(picker.pickerView) as TextWheelPickerView
        one2.isVisible = false

        val adapter = picker.pickerView!!::class.java.getDeclaredField("hourAdapter")
        adapter.isAccessible = true
        val adapter2 = adapter.get(picker.pickerView) as TextWheelAdapter
        adapter2.values = roomNumberList

        val adapterDay = picker.pickerView!!::class.java.getDeclaredField("dayAdapter")
        adapterDay.isAccessible = true
        val adapterDay2 = adapterDay.get(picker.pickerView) as TextWheelAdapter
        adapterDay2.values = roomList

        picker.setOnClickOkButtonListener {
            val pickerView = picker.pickerView ?: return@setOnClickOkButtonListener
            selRoomIdx = pickerView.day
            selRoomNumberIdx = pickerView.hour

            Logger.d("ok")
            picker.hide()
        }
        picker.setOnDismissListener {
            Toast.makeText(this, "Action Sheet Dismiss", Toast.LENGTH_SHORT).show()
        }
    }
}