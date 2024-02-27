package com.eneserkocak.randevu.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Firma(


   //val kullanicilar: List<String>,
    val firmaKodu:Int=0,
    //val kullanicilar: String="",
    val email: String="",
    val sifre:String= "",
    val firmaAdi: String ="",
    val firmaTel: String="",
    val firmaAdres:String= "",
    val firmaLogosu: ByteArray?=null

) {
    @PrimaryKey(autoGenerate = true)
    var id=0
}