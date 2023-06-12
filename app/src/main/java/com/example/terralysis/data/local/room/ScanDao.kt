package com.example.terralysis.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.terralysis.data.local.entity.ScanEntity

@Dao
interface ScanDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertScans(scan: List<ScanEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertScan(scan: ScanEntity)

    @Query("SELECT * from scans ORDER by timestamp DESC")
    fun getScanHistory() : LiveData<List<ScanEntity>>

    @Query("DELETE FROM scans")
    suspend fun deleteAll()

    @Update
    suspend fun updateScan(follow: ScanEntity)

    @Query("SELECT * FROM scans WHERE id = :imageId")
    fun getSingleResult(imageId : String) : LiveData<ScanEntity>
}