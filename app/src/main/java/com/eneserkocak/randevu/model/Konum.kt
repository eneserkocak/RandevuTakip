package com.eneserkocak.randevu.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "konum")
data class Konum(

    var firmaAdi:String="",
    var latitude:Double=0.0,
    var longitude:Double=0.0
) {
    @PrimaryKey(autoGenerate = true)
    var id=0
}