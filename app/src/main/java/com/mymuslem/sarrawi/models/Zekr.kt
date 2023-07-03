package com.mymuslem.sarrawi.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "zekr",
            foreignKeys = [ForeignKey(entity = Letters::class, childColumns = ["Name_ID"], parentColumns = ["ID"])])
data class Zekr(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("ID_zekr")
    var ID_zekr:Int,
    @ColumnInfo("Name_ID")
    var Name_ID:Int,
    @ColumnInfo("Description")
    var Description:String,
    @ColumnInfo("")
    var Discription_Filter:String,
    @ColumnInfo("hint")
    var hint:String,
    @ColumnInfo("couner")
    var couner:String,
    @ColumnInfo("Category")
    var Category:String
)