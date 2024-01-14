package com.eneserkocak.randevu.model

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey

const val HIZMET_ID = "hizmetId"
const val HIZMET_ADI = "hizmetAdi"
const val ACIKLAMA = "aciklama"
const val FIYAT = "fiyat"
const val HIZMET_UCRETI= "hizmetUcreti"


@Entity(tableName = "hizmetler")
data class Hizmet(
  // val personelId:Int=0,
    val firmaId: Int=0,
    val hizmetId: Int=0,
    val hizmetAdi: String ="",
    val aciklama: String="",
    val fiyat:Int=0,


    ) {
        @PrimaryKey(autoGenerate = true)
        var id=0
    }