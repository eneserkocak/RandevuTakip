package com.eneserkocak.randevu.model

import com.google.firebase.Timestamp

class MusteriVeresiye (

    val musteri: Musteri,
    val veresiyeSayisi : Int,
    val borc:Int,
    val randevuTime : Timestamp = Timestamp.now(),
)