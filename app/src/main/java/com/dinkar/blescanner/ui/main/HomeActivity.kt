package com.dinkar.blescanner.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dinkar.blescanner.R
import com.dinkar.blescanner.ui.area.AreaActivity
import com.dinkar.blescanner.ui.beacon.BeaconActivity
import com.dinkar.blescanner.ui.wifi.ListWifiActivity


class HomeActivity : AppCompatActivity(),View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val button1: Button = findViewById<View>(R.id.home_bt_beacon) as Button
        val button2: Button = findViewById<View>(R.id.home_bt_wifi) as Button
        val button3: Button = findViewById<View>(R.id.home_bt_location) as Button
        val button4: Button = findViewById<View>(R.id.home_bt_data) as Button
        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
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
                val intent1 = Intent(this, AreaActivity::class.java)
                startActivity(intent1)
            }
            else -> {}
        }
    }
}