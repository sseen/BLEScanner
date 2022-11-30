package com.dinkar.blescanner.ui.areaCard

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.ParcelUuid
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dinkar.blescanner.*
import com.dinkar.blescanner.data.DtArea
import com.dinkar.blescanner.data.DtHistory
import com.dinkar.blescanner.ui.dataCollect.DataCollectDetailActivity
import com.dinkar.blescanner.viewmodels.WordViewModel
import com.dinkar.blescanner.viewmodels.WordViewModelFactory
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import dev.shreyaspatil.MaterialDialog.MaterialDialog
import dev.shreyaspatil.MaterialDialog.model.TextAlignment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_area_card.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import sh.tyy.wheelpicker.DayTimePicker
import sh.tyy.wheelpicker.core.TextWheelAdapter
import sh.tyy.wheelpicker.core.TextWheelPickerView
import java.text.SimpleDateFormat
import java.util.*


open class AreaCardActivity: BaseDetailActivity() {

    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory((application as WordsApplication).repository)
    }
    private lateinit var previousDate: Date


    // Here, we have created new array list and added data to it
    val courseModelArrayList: ArrayList<CourseModel> = ArrayList()

    private var selRoomIdx:Int = 0
    private var selRoomNumberIdx:Int = 0
    private val roomNumberList = ('A'..'Z').map {
        TextWheelPickerView.Item(
            "$it",
            "$it"
        )
    }
    private val roomMap = mapOf(
        "bedroom" to "bedroom","hall" to "hall",
        "kitchen" to "kitchen","living" to "living",
        "toilet" to "toilet","wic" to "wic",
        "room_wa" to "room_wa","room_yo" to "room_yo",
        "stairs" to "stairs","other" to "other"
    )
    private val roomIndexList = listOf(
        "bedroom" , "hall",
        "kitchen" , "living",
        "toilet" , "wic",
        "room_wa" , "room_yo",
        "stairs" , "other"
    )
    private val roomList = roomIndexList.map {
        TextWheelPickerView.Item(
            it,
            it
        )
    }

    lateinit var courseAdapter:AreaCardAdapter

    private var btManager: BluetoothManager? = null
    private var btAdapter: BluetoothAdapter? = null
    private var btScanner: BluetoothLeScanner? = null
    val eddystoneServiceId: ParcelUuid = ParcelUuid.fromString("0000FEAA-0000-1000-8000-00805F9B34FB")
    var beaconSet: HashSet<Beacon> = HashSet()
    var beaconList: MutableList<Beacon> = mutableListOf()
    var beaconListBK: MutableList<DtHistory> = mutableListOf()

    var myUser = UserModel("device", "user","id")

    companion object {
        private const val REQUEST_ENABLE_BT = 1
        private const val PERMISSION_REQUEST_COARSE_LOCATION = 1
    }

    override fun onStart() {
        super.onStart()
        Logger.d("start card")
    }

    override fun onResume() {
        super.onResume()
        Logger.d("resume card")
    }

    override fun onPause() {
        super.onPause()
        Logger.d("pause card")
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.d("destroy card")
    }

    private fun setUpBluetoothManager() {
        btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        btAdapter = btManager!!.adapter
        btScanner = btAdapter?.bluetoothLeScanner
        if (btAdapter != null && !btAdapter!!.isEnabled) {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableIntent, AreaCardActivity.REQUEST_ENABLE_BT)
        }
        checkForLocationPermission()

        val bleScanSettings = ScanSettings.Builder().setScanMode(
            ScanSettings.SCAN_MODE_LOW_POWER
        ).build()
        btScanner!!.startScan(null,bleScanSettings,leScanCallback)
    }

    private fun checkForLocationPermission() {
        // Make sure we have access coarse location enabled, if not, prompt the user to enable it
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            val builder = AlertDialog.Builder(getContext())
            builder.setTitle("This app needs location access")
            builder.setMessage("Please grant location access so this app can detect  peripherals.")
            builder.setPositiveButton(android.R.string.ok, null)
            builder.setOnDismissListener {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    AreaCardActivity.PERMISSION_REQUEST_COARSE_LOCATION
                )
            }
            builder.show()
        }
    }

    suspend fun <T> Flow<List<T>>.flattenToList() =
        flatMapConcat { it.asFlow() }.toList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_area_card)
        setTitle(R.string.home_bt_location)

        var mPrefs =  getSharedPreferences(Utils.share_pre,Context.MODE_PRIVATE)
        var gson = Gson()
        var json = mPrefs.getString(Utils.user, "")

        if (gson.fromJson(json, UserModel::class.java) != null) {
            myUser = gson.fromJson(json, UserModel::class.java)

            val lblTitle = findViewById<TextView>(R.id.idTVDeviceUser)
            val deviceName =
                getString(R.string.beacon_device_user, myUser.device,myUser.userName)
            lblTitle.text = deviceName
        }

        val courseRV = findViewById<RecyclerView>(R.id.idRVArea)

//        courseModelArrayList.add(CourseModel("living room 1", 4, 1))
//        courseModelArrayList.add(CourseModel("bed room 2", 3, 1))
//        courseModelArrayList.add(CourseModel("kitchen 3", 4, 1))
        // we are initializing our adapter class and passing our arraylist to it.
        courseAdapter = AreaCardAdapter(getContext())

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        val linearLayoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)

        // in below two lines we are setting layout-manager and adapter to our recycler view.
        courseRV.layoutManager = linearLayoutManager
        courseRV.adapter = courseAdapter as AreaCardAdapter

        wordViewModel.allAreas.observe(this) { words ->
            // Update the cached copy of the words in the adapter.
            words.let { courseAdapter.submitList(it) }
        }

        // bottom buttons control
        idBt_DataCollect_finish.setOnClickListener {
            idBt_DataCollect_save.isEnabled = true
            courseAdapter.previousSelIndex = courseAdapter.nowSelIndex
            courseAdapter.nowSelIndex = -1
            courseAdapter.notifyItemChanged(courseAdapter.previousSelIndex)
        }
        idBt_DataCollect_save.setOnClickListener {
            // mSimpleDialog.show()
            val intent1 = Intent(applicationContext, DataCollectDetailActivity::class.java)
            startActivity(intent1)
        }

        courseAdapter.isTeacherData = true
        // click cell selection
        courseAdapter.setOnItemShortClickListener(object : AreaCardAdapter.OnItemClickListener {
            override fun onItemLongClick(view: View?, pos: Int) {

                if (courseAdapter.selIndex != -1) {
                    // stop
                    if (courseAdapter.selIndex == pos) {
                        courseAdapter.selIndex = pos
                        courseAdapter.notifyItemChanged(pos)

                        btScanner!!.stopScan(leScanCallback)
                        saveToDB(beaconList)
                        beaconList.clear()
                    }
                } else {
                    // start
                    setUpBluetoothManager()
                    courseAdapter.selIndex = pos
                    courseAdapter.notifyItemChanged(pos)
                }
            }
        })

        courseAdapter.setOnItemClickListener(object : AreaCardAdapter.OnItemClickListener {
            @SuppressLint("RestrictedApi")
            override fun onItemLongClick(view: View?, pos: Int) {
                val popupMenu = view?.let { PopupMenu(this@AreaCardActivity, it) }
                if (popupMenu != null) {
                    popupMenu.menuInflater.inflate(R.menu.delete, popupMenu.menu)

                    //弹出式菜单的菜单项点击事件
                    popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                        if (item.itemId == R.id.idItem_delete) {
                            courseModelArrayList.removeAt(pos)
                            courseAdapter.notifyItemRemoved(pos)
                        }
                        false
                    })

                    val one = popupMenu::class.java.getDeclaredField("mPopup")
                    one.isAccessible = true
                    val mHelper = one.get(popupMenu) as MenuPopupHelper
                    mHelper.setForceShowIcon(true)

                    // val pWindow = PopupWindow(view).apply {
                    //     isOutsideTouchable = true
                    //     isFocusable = true
                    // }
                    // pWindow.show
                    popupMenu.show()
                }
            }
        })
    }

    open fun getContext(): Context {
        return this.applicationContext
    }


    private val leScanCallback: ScanCallback = object : ScanCallback() {
        @SuppressLint("SuspiciousIndentation")
        override fun onScanResult(callbackType: Int, result: ScanResult) {

            // measureFps.AddRecord(true);

            val wifiManager =
                getContext().getSystemService(WIFI_SERVICE) as WifiManager
            val numberOfLevels = 5
            val wifiInfo = wifiManager.connectionInfo

            val scanRecord = result.scanRecord
            val beacon = Beacon(result.device.address)

            if (ActivityCompat.checkSelfPermission(
                    getContext(),
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toasty.success(getContext(), "Need Bluetooth permission", Toast.LENGTH_SHORT, true).show();
                return
            }
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
                    beacon.areaName = courseAdapter.selRoom

                    val date = Calendar.getInstance().time
                    val dateInString = date.toString("yyyy_MM_dd_HH_mm_ss_SSS")
                    if (!::previousDate.isInitialized) {
                        previousDate = date
                    }
                    beacon.date = date
                    beacon.dateStr = dateInString

//                     if (iBeaconUUID.equals("E2C56DB5DFFB48D2B060D0F5A71096E0"))
                         Log.e("DINKAR", "${beaconSet.contains(beacon)} $dateInString UUID:$iBeaconUUID major:$major minor:$minor RSSI:${result.rssi} wifi:${wifiInfo.rssi}")

                    beaconSet.add(beacon)
                    beaconList.add(beacon)

                    if (beaconList.count() > 20) {
                        saveToDB(beaconList)
                        beaconList.clear()
                    }
                }
            }
        }

        fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
            val formatter = SimpleDateFormat(format, locale)
            return formatter.format(this)
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("DINKAR", errorCode.toString())
        }
    }

    private fun saveToDB(result: List<Beacon>) {
        var arr = mutableListOf<DtHistory>()
        for (one in result) {
            val sid = one.uuid ?: ""
            val datas = one.dateStr ?: ""
            val times = one.date?.time?.minus(previousDate.time)
            previousDate = one.date!!
            val dt = times?.let {
                DtHistory(
                    0, one.areaName ?: "" ,
                    myUser.device, one.rssi ?: 0,
                    it.toInt(), myUser.userName,
                    myUser.idStr, datas,
                    sid, 1
                )
            }
            if (dt != null) {
                arr.add(dt)
            }
        }

        wordViewModel.insertHistoryAll(arr)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)

        menu?.findItem(R.id.save_item)?.setIcon(R.drawable.baseline_add_circle_white_24dp)

        return true
    }


    override fun othersOnOptionsItemSelected(item: MenuItem) {
        super.othersOnOptionsItemSelected(item)
        if (item.itemId == R.id.save_item) {
            // 直接输入detail
            // val intent1 = Intent(this, AreaDetailActivity::class.java)
            // 直接选择detail
            // val intent1 = Intent(this, AreaDetailSelectorActivity::class.java)
            // startActivity(intent1)
            // 都不用了，改为两排 picker
            showPicker()
        } else {
            Toasty.success(applicationContext, "Success!", Toast.LENGTH_SHORT, true).show()

            finish()
        }
    }

    private fun showPicker() {
        val picker = DayTimePicker(this)

        picker.show(window)
        picker.pickerView?.day = 0
        picker.pickerView?.hour = 0

        // reflect to change data source
        val one = picker.pickerView!!::class.java.getDeclaredField("minutePickerView")
        one.isAccessible = true
        val one2 = one.get(picker.pickerView) as TextWheelPickerView
        one2.isVisible = false

        val adapter = picker.pickerView!!::class.java.getDeclaredField("hourAdapter")
        adapter.isAccessible = true
        val adapter2 = adapter.get(picker.pickerView) as TextWheelAdapter
        adapter2.values = roomNumberList

        val adapterDay = picker.pickerView!!::class.java.getDeclaredField("dayAdapter")
        adapterDay.isAccessible = true
        val adapterDay2 = adapterDay.get(picker.pickerView) as TextWheelAdapter
        adapterDay2.values = roomList

        picker.setOnClickOkButtonListener {
            val pickerView = picker.pickerView ?: return@setOnClickOkButtonListener
            selRoomIdx = pickerView.day
            selRoomNumberIdx = pickerView.hour

            var room = roomIndexList[selRoomIdx-1]
            var index = roomNumberList[selRoomNumberIdx].text
            wordViewModel.insertArea(DtArea(0, room + "_" + index))
            //courseAdapter.notifyDataSetChanged()

            Logger.d("ok")
            picker.hide()
        }
        picker.setOnDismissListener {
            Toast.makeText(this, "Action Sheet Dismiss", Toast.LENGTH_SHORT).show()
        }
    }
}