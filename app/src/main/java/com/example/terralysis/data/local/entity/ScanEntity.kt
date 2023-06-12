package com.example.terralysis.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "scans")
data class ScanEntity(
    @PrimaryKey
    @field:ColumnInfo(name = "id")
    val id : String,

    @field:ColumnInfo(name = "name")
    val name : String,

    @field:ColumnInfo(name = "timestamp")
    val timestamp : String = "",

    @field:ColumnInfo(name = "uri")
    val uri : String,

    @field:ColumnInfo(name = "longDesc")
    val longDesc : String = "",

    @field:ColumnInfo(name = "shortDesc")
    val shortDesc : String = "",

    @field:ColumnInfo(name = "physicalChar")
    val physicalCar : String = "",

    @field:ColumnInfo(name = "spread")
    val spread : String = "",

    @field:ColumnInfo(name = "morphologyChar")
    val morphologyChar : String = "",

    @field:ColumnInfo(name = "property")
    val property : String = "",

    @field:ColumnInfo(name = "chemicalChar")
    val chemicalChar : String = ""
) : Parcelable