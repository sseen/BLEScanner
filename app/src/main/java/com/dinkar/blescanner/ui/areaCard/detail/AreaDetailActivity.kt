package com.dinkar.blescanner.ui.areaCard.detail

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import com.dinkar.blescanner.BaseDetailActivity
import com.dinkar.blescanner.R
import com.dinkar.blescanner.ui.areaCard.detail.ui.main.AreaDetailFragment


class AreaDetailActivity : BaseDetailActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_area_detail)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AreaDetailFragment.newInstance())
                .commitNow()
        }
    }
}