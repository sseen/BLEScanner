package com.dinkar.blescanner.ui.wifi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.dinkar.blescanner.BaseDetailActivity
import com.dinkar.blescanner.R
import com.dinkar.blescanner.ui.wifi.ui.wifi.ListWifiFragment
import es.dmoral.toasty.Toasty

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }


    override fun othersOnOptionsItemSelected(item: MenuItem) {
        super.othersOnOptionsItemSelected(item)
        if (item.itemId == R.id.save_item) {
            Toasty.success(applicationContext, "Success!", Toast.LENGTH_SHORT, true).show();

            finish()
        }
    }
}