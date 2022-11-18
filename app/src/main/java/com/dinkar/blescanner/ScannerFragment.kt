package com.dinkar.blescanner

import android.Manifest
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.ParcelUuid
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log


class ScannerFragment : Fragment() {
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var spinner: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var tvLog: EditText

    private var btManager: BluetoothManager? = null
    private var btAdapter: BluetoothAdapter? = null
    private var btScanner: BluetoothLeScanner? = null
    val eddystoneServiceId: ParcelUuid = ParcelUuid.fromString("0000FEAA-0000-1000-8000-00805F9B34FB")
    var beaconSet: HashSet<Beacon> = HashSet()
    var beaconList: MutableList<Beacon> = mutableListOf()
    var beaconTypePositionSelected = 0
    var beaconAdapter: BeaconsAdapter? = null
    var logContent: String = ""
    var wifiRssi = 0

    private val measureFps = MeasureFps(6)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scanner, container, false)
        initViews(view)
        setUpBluetoothManager()
        return view
    }

    companion object {
        private const val REQUEST_ENABLE_BT = 1
        private const val PERMISSION_REQUEST_COARSE_LOCATION = 1
    }

    private fun initViews(view: View) {
        startButton = view.findViewById(R.id.startButton)
        stopButton = view.findViewById(R.id.stopButton)
        spinner = view.findViewById(R.id.spinner)
        recyclerView = view.findViewById(R.id.recyclerView)
        tvLog = view.findViewById(R.id.log_edit_text)

        startButton.setOnClickListener { onStartScannerButtonClick() }
        stopButton.setOnClickListener { onStopScannerButtonClick() }
        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager

        beaconAdapter = BeaconsAdapter(beaconList)//beaconSet.toList())
        recyclerView.adapter = beaconAdapter

        beaconAdapter!!.filter.filter(Utils.IBEACON)
        spinner.onItemSelectedListener = (object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                tvLog.setText("")
                logContent = ""
                beaconList.clear()
                beaconTypePositionSelected = position
                setBeaconFilter(position)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                beaconAdapter!!.filter.filter(Utils.ALL)
            }

        })
        activity?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.BLE_Scanner_Type,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinner.adapter = adapter
            }
        }

        // rssi变化监听
        activity?.registerReceiver(myRssiChangeReceiver, IntentFilter(WifiManager.RSSI_CHANGED_ACTION))

        // wifi扫描结果监听
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        requireContext().registerReceiver(wifiScanReceiver, intentFilter)

        startScanWifis()
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
            val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            if (success) {
                val wifiManager = context?.getSystemService(Context.WIFI_SERVICE) as WifiManager
                Log.e("wifi scan","${wifiManager.scanResults.first().level}")
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
            wifiRssi = newRssi
        }
    }

    private fun onStartScannerButtonClick() {
        startButton.visibility = View.GONE
        stopButton.visibility = View.VISIBLE
        val bleScanSettings = ScanSettings.Builder().setScanMode(
            ScanSettings.SCAN_MODE_LOW_POWER
        ).build()
        btScanner!!.startScan(null,bleScanSettings,leScanCallback)
    }

    private fun onStopScannerButtonClick() {
        tvLog.setText(logContent)
        stopButton.visibility = View.GONE
        startButton.visibility = View.VISIBLE
        btScanner!!.stopScan(leScanCallback)
    }

    private fun setUpBluetoothManager() {
        btManager = activity?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        btAdapter = btManager!!.adapter
        btScanner = btAdapter?.bluetoothLeScanner
        if (btAdapter != null && !btAdapter!!.isEnabled) {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
        }
        checkForLocationPermission()
    }

    private fun checkForLocationPermission() {
        // Make sure we have access coarse location enabled, if not, prompt the user to enable it
        if (requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("This app needs location access")
            builder.setMessage("Please grant location access so this app can detect  peripherals.")
            builder.setPositiveButton(android.R.string.ok, null)
            builder.setOnDismissListener {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    PERMISSION_REQUEST_COARSE_LOCATION
                )
            }
            builder.show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_COARSE_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    println("coarse location permission granted")
                } else {
                    val builder = AlertDialog.Builder(activity)
                    builder.setTitle("Functionality limited")
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover BLE beacons")
                    builder.setPositiveButton(android.R.string.ok, null)
                    builder.setOnDismissListener { }
                    builder.show()
                }
                return
            }
        }
    }

    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {

            // measureFps.AddRecord(true);

            val wifiManager =
                context!!.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val numberOfLevels = 5
            val wifiInfo = wifiManager.connectionInfo

            val scanRecord = result.scanRecord
            val beacon = Beacon(result.device.address)

            beacon.manufacturer = result.device.name
            beacon.rssi = result.rssi
            if (scanRecord != null) {
                val serviceUuids = scanRecord.serviceUuids
                val iBeaconManufactureData = scanRecord.getManufacturerSpecificData(0X004c)
                if (serviceUuids != null && serviceUuids.size > 0 && serviceUuids.contains(
                        eddystoneServiceId
                    )
                ) {
                    val serviceData = scanRecord.getServiceData(eddystoneServiceId)
                    if (serviceData != null && serviceData.size > 18) {
                        val eddystoneUUID =
                            Utils.toHexString(Arrays.copyOfRange(serviceData, 2, 18))
                        val namespace = String(eddystoneUUID.toCharArray().sliceArray(0..19))
                        val instance = String(
                            eddystoneUUID.toCharArray()
                                .sliceArray(20 until eddystoneUUID.toCharArray().size)
                        )
                        beacon.type = Beacon.beaconType.eddystoneUID
                        beacon.namespace = namespace
                        beacon.instance = instance

                        Log.e("DINKAR", "Namespace:$namespace Instance:$instance")
                    }
                }
                if (iBeaconManufactureData != null && iBeaconManufactureData.size >= 23) {
                    val iBeaconUUID = Utils.toHexString(iBeaconManufactureData.copyOfRange(2, 18))
                    val major = Integer.parseInt(
                        Utils.toHexString(
                            iBeaconManufactureData.copyOfRange(
                                18,
                                20
                            )
                        ), 16
                    )
                    val minor = Integer.parseInt(
                        Utils.toHexString(
                            iBeaconManufactureData.copyOfRange(
                                20,
                                22
                            )
                        ), 16
                    )
                    beacon.type = Beacon.beaconType.iBeacon
                    beacon.uuid = iBeaconUUID
                    beacon.major = major
                    beacon.minor = minor
                    val date = Calendar.getInstance().time
                    val dateInString = date.toString("yyyy/MM/dd HH:mm:ss.SSS")

                    // if (iBeaconUUID.equals("E2C56DB5DFFB48D2B060D0F5A71096E0"))
                    //     Log.e("DINKAR", "${beaconSet.contains(beacon)} $dateInString UUID:$iBeaconUUID major:$major minor:$minor RSSI:${result.rssi} wifi:${wifiInfo.rssi}")

                    //logContent += "$dateInString UUID:$iBeaconUUID major:$major minor:$minor RSSI:${result.rssi}\n"

                    when (beaconTypePositionSelected) {
                        0 -> {
                            logContent += "$dateInString UUID:$iBeaconUUID major:$major minor:$minor RSSI:${result.rssi}\n$dateInString Wifi SSID:${wifiInfo.ssid} BSSID:${wifiInfo.ssid} RSSI:${wifiRssi}\n"
                            beaconSet.add(beacon)
                            beaconList.add(beacon)
                        }
                        1 -> {
                            logContent += "$dateInString UUID:$iBeaconUUID major:$major minor:$minor RSSI:${result.rssi}\n"
                            beaconSet.add(beacon)
                            beaconList.add(beacon)
                        }
                        else -> {
                            logContent += "$dateInString Wifi SSID:${wifiInfo.ssid} BSSID:${wifiInfo.bssid} RSSI:${wifiInfo.rssi}\n"
                            beacon.uuid = wifiInfo.ssid
                            beacon.type = Beacon.beaconType.eddystoneUID
                            beacon.rssi = wifiInfo.rssi
                            Log.v("wifi","beacon ${beacon.rssi}")
                            beaconSet.add(beacon)
                            beaconList.add(beacon)
                        }
                    }
                }
            }

            (recyclerView.adapter as BeaconsAdapter).updateData(beaconList,beaconTypePositionSelected)
        }

        fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
            val formatter = SimpleDateFormat(format, locale)
            return formatter.format(this)
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("DINKAR", errorCode.toString())
        }
    }

    fun setBeaconFilter(position: Int) {
        when (position) {
            0 -> {
                beaconAdapter!!.filter.filter(Utils.ALL)
            }
            1 -> {
                beaconAdapter!!.filter.filter(Utils.IBEACON)
            }
            2 -> {
                beaconAdapter!!.filter.filter(Utils.EDDYSTONE)
            }
        }
    }

}