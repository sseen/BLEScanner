package com.dinkar.blescanner.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.dinkar.blescanner.R
import com.dinkar.blescanner.WordsApplication
import com.dinkar.blescanner.ui.areaCard.AreaCardActivity
import com.dinkar.blescanner.ui.beacon.BeaconActivity
import com.dinkar.blescanner.ui.dataCollect.DataColloctActivity
import com.dinkar.blescanner.ui.wifi.ListWifiActivity
import com.dinkar.blescanner.viewmodels.WordViewModel
import com.dinkar.blescanner.viewmodels.WordViewModelFactory
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.dinkar.blescanner.data.Word

class HomeActivity : AppCompatActivity(),View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Logger.addLogAdapter(AndroidLogAdapter())


        val button1: Button = findViewById<View>(R.id.home_bt_beacon) as Button
        val button2: Button = findViewById<View>(R.id.home_bt_wifi) as Button
        val button3: Button = findViewById<View>(R.id.home_bt_location) as Button
        val button4: Button = findViewById<View>(R.id.home_bt_data) as Button
        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        Logger.d("start home")
    }

    override fun onResume() {
        super.onResume()
        Logger.d("resume home")
    }

    override fun onPause() {
        super.onPause()
        Logger.d("pause home")
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.d("destroy home")
    }

    override fun onClick(p0: View?) {

        when (p0?.id) {
            R.id.home_bt_beacon -> {
                val intent1 = Intent(this, BeaconActivity::class.java)
                startActivity(intent1)
            }
            R.id.home_bt_wifi -> {
                val intent1 = Intent(this, ListWifiActivity::class.java)
                startActivity(intent1)
            }
            R.id.home_bt_location -> {
                // val intent1 = Intent(this, AreaActivity::class.java)
                val intent1 = Intent(this, AreaCardActivity::class.java)
                startActivity(intent1)
            }
            else -> {
                val intent1 = Intent(this, DataColloctActivity::class.java)
                startActivity(intent1)
            }
        }
    }
}