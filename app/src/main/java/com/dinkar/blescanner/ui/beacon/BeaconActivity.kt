package com.dinkar.blescanner.ui.beacon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.dinkar.blescanner.BaseDetailActivity
import com.dinkar.blescanner.R
import kotlinx.android.synthetic.main.activity_home.*

class BeaconActivity : BaseDetailActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beacon)
        setTitle(R.string.home_bt_beacon)
    }

}