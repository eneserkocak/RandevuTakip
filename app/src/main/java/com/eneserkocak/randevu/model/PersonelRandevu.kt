package com.eneserkocak.randevu.model

import com.google.firebase.Timestamp

data class PersonelRandevu(
    val personel: Personel,
    //val randevular:Randevu,
    val randevuSayisi : Int,
    val gelir:Int,
    val randevuTime : Timestamp = Timestamp.now(),
)