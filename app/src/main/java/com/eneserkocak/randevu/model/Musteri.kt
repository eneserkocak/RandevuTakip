package com.eneserkocak.randevu.model

import androidx.room.Entity
import androidx.room.PrimaryKey




@Entity(tableName = "musteriler")
data class Musteri(

    val firmaKodu: Int = 0,
    val musteriId: Int=0,
    val musteriAdi: String ="",
    val musteriTel: String="",
    val musteriMail:String="",
    val musteriAdres:String="",
    val musteriNot:String="",
    val musteriGorsel:String="",
    val musteriVeresiye:Boolean =false,
    val musteriBorc: Int =0
){
    @PrimaryKey(autoGenerate = true)
    var id=0
}