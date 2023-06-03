package com.example.terralysis.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.terralysis.data.local.entity.ScanEntity

@Dao
interface ScanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScan(story : List<ScanEntity>)

    @Query("SELECT * from stories")
    fun getScanHistory() : LiveData<List<ScanEntity>>

    @Query("DELETE FROM stories")
    suspend fun deleteAll()
}