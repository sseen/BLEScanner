package com.dinkar.blescanner.ui.dataCollect

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.dinkar.blescanner.BaseDetailActivity
import com.dinkar.blescanner.R
import com.dinkar.blescanner.UserModel
import com.dinkar.blescanner.Utils
import com.dinkar.blescanner.ui.main.HomeActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_data_collect_detail.*


class DataCollectDetailActivity : BaseDetailActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_collect_detail)

        setTitle(R.string.home_bt_location)

        var mPrefs =  getSharedPreferences(Utils.share_pre, Context.MODE_PRIVATE)
        var gson = Gson()
        var json = mPrefs.getString(Utils.user, "")
        var myUser = gson.fromJson(json, UserModel::class.java)

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
            val intent1 = Intent(this, HomeActivity::class.java)
            startActivity(intent1)
        }
    }
}