package com.dinkar.blescanner.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_table")
class Word(

    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "word") val word: String

)

@Entity(tableName = "dt_history_table")
class DtHistory(

    @PrimaryKey(autoGenerate = true) val id: Int,
    val areaname: String,
    val setting_facilities: String,
    val dBm: Int,
    @ColumnInfo(name = "time(ms)") val times: Int,
    val setting_name: String,
    val setting_note: String,
    @ColumnInfo(name = "data") val datas: String,
    @ColumnInfo(name = "BLE/Wifi") val ids: String,
    val type: Int, // 0 beacon 1 wifi
    val major: String, //
    val minor: String, //
    //val userName: String, //
)

@Entity(tableName = "dt_area_table")
class DtArea(

    @PrimaryKey(autoGenerate = true) val id: Int,
    val areaname: String,
)