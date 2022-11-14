package com.dinkar.blescanner.ui.dataCollect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.dinkar.blescanner.R
import com.dinkar.blescanner.ui.areaCard.AreaCardActivity

class DataColloctActivity : AreaCardActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)

        val item = menu?.findItem(R.id.save_item) as MenuItem
        item.isVisible = false

        return true
    }
}