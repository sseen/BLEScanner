package com.dinkar.blescanner.ui.wifi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dinkar.blescanner.BaseDetailActivity
import com.dinkar.blescanner.R
import com.dinkar.blescanner.ui.wifi.ui.wifi.ListWifiFragment

class ListWifiActivity : BaseDetailActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_wifi)
        setTitle(R.string.home_bt_wifi)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ListWifiFragment.newInstance())
                .commitNow()
        }
    }
}