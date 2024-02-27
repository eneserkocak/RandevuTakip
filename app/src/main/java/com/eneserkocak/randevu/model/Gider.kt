package com.eneserkocak.randevu.model

import com.google.firebase.Timestamp
import java.util.*

data class Gider(
    val firmaKodu: Int=0,
    val giderId:Int=0,
    val giderAdi: String ="",
    val giderTutar:Int=0,
    val giderTarih: Timestamp = Timestamp.now(),
) {
}