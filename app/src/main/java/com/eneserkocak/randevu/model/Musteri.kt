package com.eneserkocak.randevu.model

import androidx.room.Entity
import androidx.room.PrimaryKey


const val MUSTERI_ID = "musteriId"
const val MUSTERI_ADI = "musteriAdi"
const val MUSTERI_TEL = "musteriTel"
const val MUSTERI_MAIL = "musteriMail"
const val MUSTERI_ADRES = "musteriAdres"
const val MUSTERI_NOT = "musteriNot"
const val MUSTERI_GORSEL= "musteriGorsel"

@Entity(tableName = "musteriler")
data class Musteri(

    val firmaId: Int = 0,
    val musteriId: Int=0,
    val musteriAdi: String ="",
    val musteriTel: String="",
    val musteriMail:String="",
    val musteriAdres:String="",
    val musteriNot:String="",
    val musteriGorsel:String=""
){
    @PrimaryKey(autoGenerate = true)
    var id=0
}