package com.dinkar.blescanner.ui.dataCollect

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.dinkar.blescanner.*
import com.dinkar.blescanner.data.WordRoomDatabase
import com.dinkar.blescanner.ui.main.HomeActivity
import com.dinkar.blescanner.viewmodels.WordViewModel
import com.dinkar.blescanner.viewmodels.WordViewModelFactory
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_data_collect_detail.*


class DataCollectDetailActivity : BaseDetailActivity() {

    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory((application as WordsApplication).repository)
    }

    private val REQUEST_EXTERNAL_STORAGE = 1

    private val PERMISSIONS_STORAGE = arrayOf(
        "android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.WRITE_EXTERNAL_STORAGE"
    )

    fun verifyStoragePermissions(activity: Activity) {
        try {

            //检测是否有写的权限
            val permission: Int = ActivityCompat.checkSelfPermission(
                activity,
                "android.permission.WRITE_EXTERNAL_STORAGE"
            )
            if (permission != PackageManager.PERMISSION_GRANTED) {

                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_collect_detail)

        setTitle(R.string.home_bt_data)

        val extra = intent.extras
        var isTeacher = true
        if (extra != null) {
            val type = extra.getString(Utils.kTearcherOrData)
            if (type == "data") {
                isTeacher = false
            }
        }

        val mPrefs =  getSharedPreferences(Utils.share_pre, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = mPrefs.getString(Utils.user, "")
        val myUser = gson.fromJson(json, UserModel::class.java)

        if (myUser != null) {
            val lblTitle = findViewById<TextView>(R.id.idTV_DataCollect_user)
            val deviceName =
                getString(R.string.beacon_device_user, myUser.device,myUser.userName)
            lblTitle.text = deviceName
        }

        idBt_DataCollect_cancel.setOnClickListener {
            finish()
        }
        idBt_DataCollect_done.setOnClickListener {
            val dbSave = One(isTeacher)
            dbSave.setListener {
                wordViewModel.clearHistory()
                val intent1 = Intent(this, HomeActivity::class.java)
                startActivity(intent1)
            }
            dbSave.exportDatabase()

        }

        verifyStoragePermissions(this@DataCollectDetailActivity)
    }
}