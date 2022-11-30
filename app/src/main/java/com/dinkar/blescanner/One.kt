package com.dinkar.blescanner

import android.R.attr.delay
import android.annotation.SuppressLint
import android.os.Environment
import android.os.Handler
import com.dinkar.blescanner.data.WordRoomDatabase
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.text.DateFormat
import java.util.*


class One(val isTeacher:Boolean) {
    lateinit var mListener:(String)->Unit
    fun setListener(listener:(String)->Unit) {
        this.mListener = listener
    }

    @SuppressLint("Range")
    public fun exportDatabase(type: Int): Boolean {
        val df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())

        /**First of all we check if the external storage of the device is available for writing.
         * Remember that the external storage is not necessarily the sd card. Very often it is
         * the device storage.
         */
        val state = Environment.getExternalStorageState()
        return if (Environment.MEDIA_MOUNTED != state) {
            false
        } else {
            //We use the Download directory for saving our .csv file.
//            val exportDir =
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

            var file: File
            var printWriter: PrintWriter? = null
            try {

                /**This is our database connector class that reads the data from the database.
                 * The code of this class is omitted for brevity.
                 */
                val db: WordRoomDatabase? = WordRoomDatabase.getSQliteDB()

                /**Let's read the first table of the database.
                 * getFirstTable() is a method in our DBCOurDatabaseConnector class which retrieves a Cursor
                 * containing all records of the table (all fields).
                 * The code of this class is omitted for brevity.
                 */
                // beacon
                val curGroup = db?.query("select * from dt_history_table where type=$type group by \"BLE/Wifi\"", null)
                if (curGroup != null) {
                    var groupIndex = 1
                    while (curGroup.moveToNext()) {
                        val data = curGroup.getString(curGroup.getColumnIndex("data"))
                        val name = curGroup.getString(curGroup.getColumnIndex("setting_name"))
                        val deviceName = curGroup.getString(curGroup.getColumnIndex("setting_facilities"))
                        val deviceNote = curGroup.getString(curGroup.getColumnIndex("setting_note"))
                        val bleWifi = curGroup.getString(curGroup.getColumnIndex("BLE/Wifi"))
                        var wifiFolder = if (type == 1) "ble$groupIndex" else "wifi$groupIndex"
                        var fileName = "${deviceName}_${name}_${deviceNote}_${wifiFolder}_$data.csv"
                        SSLog.p(data +  " " + bleWifi)
                        var typeFolder = if (isTeacher) "teacherData" else "testData"

                        // 得到 beacon list
                        // 创建文件名
                        val exportDir = File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path +
                                    File.separator + "BLEScanner" +
                                    File.separator + name +
                                    File.separator + typeFolder +
                                    File.separator + wifiFolder
                        )

                        if (!exportDir.exists()) {
                            exportDir.mkdirs()
                        }
                        // 打开文件
                        file = File(exportDir, "$fileName")
                        file.createNewFile()
                        printWriter = PrintWriter(FileWriter(file))
                        // 保存
                        val curCSV = db?.query("select * from dt_history_table where \"BLE/Wifi\"='$bleWifi' and type=$type", null)
                        //Write the name of the table and the name of the columns (comma separated values) in the .csv file.
                        printWriter.println("FIRST TABLE OF THE DATABASE")
                        printWriter.println("Time(ms),areaname,dBm,setting_facilities,setting_name," +
                                "setting_note,data,BLE/Wifi")
                        if (curCSV != null) {
                            while (curCSV.moveToNext()) {
                                @SuppressLint("Range")
                                val sTime = curCSV.getLong(curCSV.getColumnIndex("time(ms)"))
                                val areaName = curCSV.getString(curCSV.getColumnIndex("areaname"))
                                val dBm = curCSV.getFloat(curCSV.getColumnIndex("dBm"))
                                val major = curCSV.getString(curCSV.getColumnIndex("major"))
                                val minor = curCSV.getString(curCSV.getColumnIndex("minor"))
                                val setting_facilities = curCSV.getString(curCSV.getColumnIndex("setting_facilities"))
                                val setting_name = curCSV.getString(curCSV.getColumnIndex("setting_name"))
                                val setting_note = curCSV.getString(curCSV.getColumnIndex("setting_note"))
                                val data = curCSV.getString(curCSV.getColumnIndex("data"))
                                val bleWifi = curCSV.getString(curCSV.getColumnIndex("BLE/Wifi"))

                                /**Create the line to write in the .csv file.
                                 * We need a String where values are comma separated.
                                 * The field date (Long) is formatted in a readable text. The amount field
                                 * is converted into String.
                                 */
                                val record = "$sTime" + "," + areaName + "," + "$dBm" + "," +
                                        "$major" + "," + "$minor" + "," +
                                        setting_facilities + "," + setting_name + "," + setting_note + "," +
                                        data + "," + bleWifi
                                printWriter.println(record) //write the record in the .csv file
                            }
                        }
                        if (curCSV != null) {
                            groupIndex += 1
                            curCSV.close()
                        }
                    }
                }
                if (curGroup != null) {
                    curGroup.close()
                }
                if (db != null) {
                    //db.close()
                }
                if (type == 2)
                    mListener("")
//                Handler().postDelayed(Runnable {
//                    //execute the task
//                    mListener("")
//                }, 500)
            } catch (exc: Exception) {
                //if there are any exceptions, return false
                return false
            } finally {
                printWriter?.close()

            }

            //If there are no errors, return true.
            true
        }
    }
}