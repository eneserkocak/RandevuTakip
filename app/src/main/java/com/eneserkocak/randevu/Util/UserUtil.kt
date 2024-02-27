package com.eneserkocak.randevu.Util

import androidx.lifecycle.MutableLiveData
import com.eneserkocak.randevu.model.Firma
import kotlin.properties.Delegates

object UserUtil {


    //Auth create de değer eşitle. Tüm uygulamada bu firmaKodu KULLANILACAK...!!!
   var firmaKodu :Int=100

    //Auth create de İlk kullanıcı "PATRON" un  UID si:
   var uid:String=""

    //PERS HESAP EKLE FRAGMENT TA DEĞERİNİ AL: TÜM KULLANICI UİD lerinin İÇİNDE OLDUĞU ARRAYLİST
    lateinit var kullanicilar:ArrayList<String>

    //firma adını SMS GÖNDER FRAGMENT TA BURDAN AL KULLAN..
    var firmaIsim:String=""



}