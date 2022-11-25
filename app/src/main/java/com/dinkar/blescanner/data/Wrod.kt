package com.dinkar.blescanner.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_table")
class Word(

    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "word") val word: String

)

@Entity(tableName = "data_history_table")
class DataHistory(

    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "word") val word: String

)