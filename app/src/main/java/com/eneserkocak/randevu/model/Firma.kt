package com.eneserkocak.randevu.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp

@Entity
data class Firma(


   //val kullanicilar: List<String>,
    val firmaKodu:Int=0,
    //val kullanicilar: String="",
    val email: String="",
    var sifre:String= "",
    val firmaAdi: String ="",
    val firmaTel: String="",
    val firmaAdres:String= "",
    val firmaLogosu: ByteArray?=null,
    val skt: Timestamp = Timestamp.now()

    ) {
    @PrimaryKey(autoGenerate = true)
    var id=0
}