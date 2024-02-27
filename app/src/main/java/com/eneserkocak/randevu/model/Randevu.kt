package com.eneserkocak.randevu.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize
import java.util.*



data class Randevu(
    val firmaKodu:Int,
    val personel: Personel,
    val musteri: Musteri,
    val hizmet: Hizmet,
    val randevuGeliri:Int=0,
    val randevuTime : Timestamp=Timestamp.now(),
    var randevuDurumu:String="",
    var randevuNotu:String="",
    var randevuGelirTuru:String="",
    var veresiyeTutari:Int=0

    ) {}


