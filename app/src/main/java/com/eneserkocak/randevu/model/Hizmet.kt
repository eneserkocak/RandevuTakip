package com.eneserkocak.randevu.model

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "hizmetler")
data class Hizmet(
  // val personelId:Int=0,
    val firmaKodu: Int=0,
    val hizmetId: Int=0,
    val hizmetAdi: String ="",
    val aciklama: String="",
    val fiyat:Int=0,
    var hizmetGorDur:Boolean = true,


    ) {
        @PrimaryKey(autoGenerate = true)
        var id=0
    }