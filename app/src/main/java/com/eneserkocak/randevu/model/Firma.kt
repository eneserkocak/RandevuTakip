package com.eneserkocak.randevu.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Firma(

    val firmaAdi: String ="",
    val telefonNumarasi: String="",
    val adres:String= "",
    val firmaLogosu: ByteArray?=null

) {
    @PrimaryKey(autoGenerate = true)
    var id=0
}