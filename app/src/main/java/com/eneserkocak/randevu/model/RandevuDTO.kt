package com.eneserkocak.randevu.model

import com.google.firebase.Timestamp
import java.util.*

data class RandevuDTO(
    val randevuId:Int=0,
    val firmaKodu: Int = 0,
    val personelId: Int=0,
    val musteriId : Int = 0,
    val hizmetId : Int = 0,
    val hizmetUcreti:Int=0,
    val randevuGeliri:Int=0,
    val randevuTime : Timestamp = Timestamp.now(),
    val randevuDurumu:String="",
    val randevuNotu:String="",
    var randevuGelirTuru:String="",
    var veresiyeTutari:Int=0
)