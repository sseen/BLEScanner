package com.dinkar.blescanner

import android.util.Log

/**
 * Created by jackf on 2014/6/10.
 */
class MeasureFps(var mMaxRecord: Int) {
    val TAG = "MeasureFps"
    var mTimeRecords: LongArray
    var mValidRecordCount: Int

    init {
        mTimeRecords = LongArray(mMaxRecord)
        mValidRecordCount = 0
    }

    fun AddRecord(autoDump: Boolean): Boolean {
        if (mValidRecordCount >= mMaxRecord) {
            if (autoDump == false) return false
            DumpStatistics(true)
        }
        mTimeRecords[mValidRecordCount] = System.currentTimeMillis()
        mValidRecordCount++
        return true
    }

    fun DumpStatistics(reset: Boolean) {
        if (mValidRecordCount > 1) {
            val avgFrameTime =
                (mTimeRecords[mValidRecordCount - 1] - mTimeRecords[0]).toFloat() / (mValidRecordCount - 1)
            Log.i(TAG, "Average frame time = $avgFrameTime ms")
        } else {
            Log.w(TAG, "fps=?  num of valid record:$mValidRecordCount")
        }
        if (reset) mValidRecordCount = 0
        return
    }
}