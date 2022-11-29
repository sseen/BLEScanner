package com.dinkar.blescanner.ui.wifi.ui.wifi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dinkar.blescanner.SSLog
import com.dinkar.blescanner.databinding.FragmentListWifiBinding
import com.dinkar.blescanner.ui.wifi.MSArrayAdapter


class ListWifiFragment : Fragment() {
    private lateinit var binding: FragmentListWifiBinding
    var wifiLists: Array<String> = arrayOf()
    lateinit var adapter:MSArrayAdapter

    companion object {
        fun newInstance() = ListWifiFragment()
    }

    private lateinit var viewModel: ListWifiViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListWifiViewModel::class.java)

        // rssi变化监听
        activity?.registerReceiver(myRssiChangeReceiver, IntentFilter(WifiManager.RSSI_CHANGED_ACTION))

        // wifi扫描结果监听
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        requireContext().registerReceiver(wifiScanReceiver, intentFilter)

//        startScanWifis()
    }

    private fun scanSuccess() {
        //val results = wifiManager.scanResults
        startScanWifis()
    }

    private fun scanFailure() {
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        //val results = wifiManager.scanResults
        startScanWifis()
    }

    private fun startScanWifis() {
        val wifiManager = context?.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.startScan()
    }

    val wifiScanReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            Log.e("wifi scan","received")
            val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            if (success) {
                val wifiManager = context?.getSystemService(Context.WIFI_SERVICE) as WifiManager
                if (wifiManager.scanResults.isNotEmpty()) {
                    Log.e("wifi scan", "${wifiManager.scanResults.first().level}")
                    var slist = mutableListOf<String>()
                    for (one in wifiManager.scanResults) {
                        slist.add(one.SSID)
                    }
                    wifiLists = slist.toTypedArray()
                    adapter.notifyDataSetChanged()
                }
                else
                    Log.e("wifi scan","list empty")
                scanSuccess()
            } else {
                scanFailure()
            }
        }
    }


    private val myRssiChangeReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(arg0: Context, arg1: Intent) {
            val wifiMan = context!!.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val newRssi = wifiMan.connectionInfo.rssi
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListWifiBinding.inflate(inflater, container, false)
        // binding.listWifiDetail.layoutManager = LinearLayoutManager(requireContext())

        val texts = arrayOf("abc ", "bcd", "cde", "def", "efg",
            "fgh", "ghi", "hij", "ijk", "jkl", "klm","lmn","mno","nop",
            "opq","pqr","qrs","rst","stu","tuv","uvw","vwx","wxy","xyz")
        adapter = MSArrayAdapter(requireContext(), android.R.layout.simple_list_item_multiple_choice, wifiLists)
        binding.listWifiDetail.adapter = adapter
        binding.listWifiDetail.setOnItemClickListener{ parent, view, position, id ->
            SSLog.p(position.toString() + " ${binding.listWifiDetail.checkedItemIds.count()}")
        }

        return binding.root
    }

}

