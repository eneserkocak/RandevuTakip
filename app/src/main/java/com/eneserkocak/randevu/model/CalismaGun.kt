package com.eneserkocak.randevu.model

import java.util.*

data class CalismaGun(
    val gun:Int= 0, // day of week
    var calisiyormu: Boolean=true,
    var baslangicSaat:Zaman= Zaman(8,30),
    var bitisSaat:Zaman=Zaman(18,0),

   )
enum class Gunler (val int: Int){
     Pazartesi(2),Salı(3),Çarşamba(4),Perşembe(5),Cuma(6),Cumartesi(7),Pazar(1)
 }

data class Zaman(
    var saat:Int=0,
    var dakika:Int=0
)

data class RandevuSaati(
    val saat : Date,
    val doluMu : Boolean
)

