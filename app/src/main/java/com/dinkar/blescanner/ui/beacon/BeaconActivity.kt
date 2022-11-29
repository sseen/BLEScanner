package com.dinkar.blescanner.ui.beacon

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.dinkar.blescanner.BaseDetailActivity
import com.dinkar.blescanner.R
import com.dinkar.blescanner.UserModel
import com.dinkar.blescanner.Utils
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.gson.Gson
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_home.*


class BeaconActivity : BaseDetailActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beacon)
        setTitle(R.string.home_bt_beacon)


        val tvDevice = findViewById<View>(R.id.beacon_device_name) as EditText
        val tvUserName = findViewById<View>(R.id.beacon_user_name) as EditText
        val tvOther = findViewById<View>(R.id.beacon_other_name) as EditText

        var mPrefs =  getSharedPreferences(Utils.share_pre,Context.MODE_PRIVATE)
        var gson = Gson()
        var json = mPrefs.getString(Utils.user, "")
        var myUser = gson.fromJson(json, UserModel::class.java)

        if (myUser != null) {
            if (myUser.device.isNotEmpty()) {
                tvDevice.setText(myUser.device)
            }
            if (myUser.userName.isNotEmpty()) {
                tvUserName.setText(myUser.userName)
            }
            if (myUser.idStr.isNotEmpty()) {
                tvOther.setText(myUser.idStr)
            }
        }


        // get reference to button
        val btn_click_me = findViewById<View>(R.id.beacon_bt_done) as Button
        // set on-click listener
        btn_click_me.setOnClickListener {
            var one = UserModel()
            one.device = tvDevice.text.toString()
            one.userName = tvUserName.text.toString()
            one.idStr = tvOther.text.toString()

            // save to local
            var editer = mPrefs.edit()
            var oneGson = Gson()
            var jsonStr = oneGson.toJson(one)
            editer.putString(Utils.user, jsonStr)
            editer.commit()

            Toasty.success(applicationContext, "Success!", Toast.LENGTH_SHORT, true).show();
            finish()
        }
    }

}