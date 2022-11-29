package com.dinkar.blescanner

import android.annotation.SuppressLint
import android.os.Environment
import com.dinkar.blescanner.data.WordRoomDatabase
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.text.DateFormat
import java.util.*

class One {



    @SuppressLint("Range")
    public fun exportDatabase(): Boolean {
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
            val exportDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path +
                        File.separator + "BLE scanner"
            )

            if (!exportDir.exists()) {
                exportDir.mkdirs()
            }
            val file: File
            var printWriter: PrintWriter? = null
            try {
                file = File(exportDir, "MyCSVFile.csv")
                file.createNewFile()
                printWriter = PrintWriter(FileWriter(file))
                /**This is our database connector class that reads the data from the database.
                 * The code of this class is omitted for brevity.
                 */
                val db: WordRoomDatabase? = WordRoomDatabase.getSQliteDB()

                /**Let's read the first table of the database.
                 * getFirstTable() is a method in our DBCOurDatabaseConnector class which retrieves a Cursor
                 * containing all records of the table (all fields).
                 * The code of this class is omitted for brevity.
                 */
                val curCSV = db?.query("select * from dt_history_table", null)
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
                                setting_facilities + "," + setting_name + "," + setting_note + "," +
                                data + "," + bleWifi
                        printWriter.println(record) //write the record in the .csv file
                    }
                }
                if (curCSV != null) {
                    curCSV.close()
                }
                if (db != null) {
                    db.close()
                }
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