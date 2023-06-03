package com.example.terralysis.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scans")
data class ScanEntity(
    @PrimaryKey
    @field:ColumnInfo(name = "id")
    val id : String,

    @field:ColumnInfo(name = "name")
    val name : String,

    @field:ColumnInfo(name = "timestamp")
    val timestamp : String,

    @field:ColumnInfo(name = "uri")
    val uri : String,

    @field:ColumnInfo(name = "longDesc")
    val longDesc : String,

    @field:ColumnInfo(name = "shortDesc")
    val shortDesc : String
)