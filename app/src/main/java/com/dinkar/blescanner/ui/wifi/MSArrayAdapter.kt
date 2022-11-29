package com.dinkar.blescanner.ui.wifi

import android.content.Context
import android.widget.ArrayAdapter

 class MSArrayAdapter(
    context: Context?,
    textViewResourceId: Int,
    target: List<String>
) :
    ArrayAdapter<String>(context!!, textViewResourceId, target) {

    override fun hasStableIds(): Boolean {
        return true
    }
}