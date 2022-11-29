package com.dinkar.blescanner

import android.content.Context
import com.google.gson.Gson
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import android.content.SharedPreferences

object SSLog {
    fun p(string: String) {
        Logger.d(string)
    }
}

object Utils {
    val user = "user"
    val share_pre = "PREFERENCE_NAME"

    val ALL = "all"
    val EDDYSTONE = "addystone"
    val WIFI = "wifi"
    val IBEACON = "iBeacon"
    private val HEX = "0123456789ABCDEF".toCharArray()

    fun toHexString(bytes: ByteArray): String {
        if (bytes.isEmpty()) {
            return ""
        }
        val hexChars = CharArray(bytes.size * 2)
        for (j in bytes.indices) {
            val v = (bytes[j].toInt() and 0xFF)
            hexChars[j * 2] = HEX[v ushr 4]
            hexChars[j * 2 + 1] = HEX[v and 0x0F]
        }
        return String(hexChars)
    }

    fun isZeroed(bytes: ByteArray): Boolean {
        for (b in bytes) {
            if (b.toInt() != 0x00) {
                return false
            }
        }
        return true
    }

    fun getBeaconFilterFromString(optionSelected: String): Beacon.beaconType {
        return when (optionSelected) {
            IBEACON -> {
                Beacon.beaconType.iBeacon
            }
            EDDYSTONE -> {
                Beacon.beaconType.eddystoneUID
            }
            else -> {
                Beacon.beaconType.any
            }
        }
    }
}