package com.dinkar.blescanner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_main)
        loadScannerFragment()
    }

    private fun loadScannerFragment() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.your_placeholder, ScannerFragment())
        ft.commit()
    }
}