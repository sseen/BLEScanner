package com.dinkar.blescanner.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    @Query("SELECT * FROM word_table ORDER BY word ASC")
    fun getAlphabetizedWords(): Flow<List<Word>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word)

    @Query("DELETE FROM word_table")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll(users: List<Word>)

    @Insert
    suspend fun insertBatchAll(users: List<DtHistory>)

    @Query("DELETE FROM dt_history_table")
    suspend fun deleteHistoryAll()

    @Insert
    suspend fun insertArea(area: DtArea)

    @Query("DELETE FROM dt_area_table WHERE areaname = :areaName")
    fun deleteByAreaName(areaName: String)

    @Query("SELECT * FROM dt_area_table ORDER BY areaname ASC")
    fun getAlphabetizedAreas(): Flow<List<DtArea>>

    @Query("SELECT * FROM dt_area_table ORDER BY areaname ASC")
    fun getAlphabetizedAreasNoflow(): List<DtArea>
}
