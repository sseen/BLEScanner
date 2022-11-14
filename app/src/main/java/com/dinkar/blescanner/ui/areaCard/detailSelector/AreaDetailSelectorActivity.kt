package com.dinkar.blescanner.ui.areaCard.detailSelector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dinkar.blescanner.R
import com.dinkar.blescanner.ui.areaCard.detailSelector.ui.main.AreaDetailSelectorFragment
import com.dinkar.blescanner.ui.wifi.ListWifiActivity

class AreaDetailSelectorActivity : ListWifiActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_area_detail_selector)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AreaDetailSelectorFragment.newInstance())
                .commitNow()
        }
    }
}