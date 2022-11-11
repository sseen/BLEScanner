package com.dinkar.blescanner.ui.area

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dinkar.blescanner.BaseDetailActivity
import com.dinkar.blescanner.R
import com.dinkar.blescanner.ui.area.ui.main.AreaFragment

class AreaActivity : BaseDetailActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_area)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AreaFragment.newInstance())
                .commitNow()
        }
    }
}