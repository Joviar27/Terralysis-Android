package com.example.terralysis.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.terralysis.data.local.entity.ScanEntity

@Database(
    entities = [ScanEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ScanDatabase : RoomDatabase() {

    abstract fun scanDao() : ScanDao

    companion object {
        @Volatile
        private var INSTANCE: ScanDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): ScanDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ScanDatabase::class.java, "scans_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}