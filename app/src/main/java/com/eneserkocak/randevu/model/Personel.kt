package com.eneserkocak.randevu.model

import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eneserkocak.randevu.Util.AppUtil
import com.google.firebase.Timestamp
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize



data class Personel(
    val firmaId: Int = 0,
    val personelId: Int=0,
    val personelAdi: String ="",
    val personelTel: String="",
    val personelMail:String= "",
    val personelUnvan:String= "",
   // var personelGorsel:String="",

    var personelRandDur:Boolean = false,
    val personelCalismaGun:List<CalismaGun> = AppUtil.defaultCalismaGunListesi(),
    var personelCalismaDakika: Int = 0,


) {}
//Seçilen Personelin Çalışma Saatlerini Almak İçin Oluşturulan Sınıf:
/*data class Randevu(
    val firmaId: Int = 0,
    val personelId: Int=0,
    val randevuTime : Timestamp = Timestamp.now(),

    var doluMu : Boolean = false

){}*/









