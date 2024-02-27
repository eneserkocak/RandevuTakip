package com.eneserkocak.randevu.Util

import android.graphics.Color
import android.icu.text.Transliterator.Position
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.model.*
import com.google.android.material.card.MaterialCardView
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

//VERİTABANINDA kayıtlı bir değeri..XML içinde başka bir değerle almak için BindingAdapter KULLANILIYOR..


object BindingAdapter {

//AŞAĞIDA TARİH VE SAATLERİ XML İÇİNDE RANDEVUTİME (Timestamp) old için parçalayıp alamıyorduk..
    //Aşağıdaki fonksiyonlar sayesinden içinden istediğimizi alacağız.
    // (Personel Randevu Row,Müşteri Randevu Row,Gün özeti de KULLANILDI)
//constructor içinde TextView içinde kullanılacağını ve değerin Timestamp old yazıyorruz (textView: TextView, timestamp: Timestamp)
        @JvmStatic
        @BindingAdapter("app:tarihiYazdir")
        fun tarihiGetir(textView: TextView, timestamp: Timestamp){
            val sdf = SimpleDateFormat("dd.MM.yyyy")
            val tarih = sdf.format(timestamp.toDate())
            textView.text = tarih
        }

    @JvmStatic
    @BindingAdapter("app:saatiYazdir")
    fun saatiGetir(textView: TextView, timestamp: Timestamp){
        val sdf = SimpleDateFormat("HH:mm")
        val saat = sdf.format(timestamp.toDate())
        textView.text = saat
    }

    @JvmStatic
    @BindingAdapter("app:intToGunAdi")
    fun gunuGetir(textView: TextView, i : Int){
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, i)
        val simpleDateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        textView.text = simpleDateFormat.format(calendar.time)
    }

    @JvmStatic
    @BindingAdapter("app:randevuBackground")
    fun randevuBackground(materialCardView: MaterialCardView, randevu: Randevu){
      val bgResId = when(randevu.randevuDurumu) {
          TAMAMLANDI -> R.drawable.bg_randevu_tamamlandi
          IPTAL_EDILDI -> R.drawable.bg_randevu_iptal
          else -> R.drawable.home_ktphn_rectangle

      }

      materialCardView.setBackgroundResource(bgResId)

    }

    @JvmStatic
    @BindingAdapter("app:randevuTextChange")
    fun randevuTextChange(textView: TextView, randevu: Randevu){
        if (randevu.randevuDurumu== TAMAMLANDI || randevu.randevuDurumu== IPTAL_EDILDI){
            textView.visibility=View.GONE
        }else{
            textView.visibility=View.VISIBLE
        }
    }

    @JvmStatic
    @BindingAdapter("app:randevuTamTextGoster")
    fun randevuTamTextGoster(textView: TextView, randevu: Randevu){
        if (randevu.randevuDurumu== TAMAMLANDI){
            textView.visibility=View.VISIBLE
            textView.setText("Randevu Tamamlandı")
        }else{
            textView.visibility=View.GONE
        }
    }

    @JvmStatic
    @BindingAdapter("app:randevuIptTextGoster")
    fun randevuIptTextGoster(textView: TextView, randevu: Randevu){
        if (randevu.randevuDurumu== IPTAL_EDILDI){
            textView.visibility=View.VISIBLE
            textView.setText("Randevu İptal Edildi")
        }else{
            textView.visibility=View.GONE
        }
    }
    @JvmStatic
    @BindingAdapter("app:smsTextVisibility")
    fun smsTextVisibility(textView: TextView, randevu: Randevu){
        if (randevu.randevuDurumu== TAMAMLANDI || randevu.randevuDurumu== IPTAL_EDILDI){
            textView.visibility=View.GONE
        }else{
            textView.visibility=View.VISIBLE
        }
    }

    @JvmStatic
    @BindingAdapter("app:smsIconVisibility")
    fun smsIconVisibility(textView: TextView, randevu: Randevu){
        if (randevu.randevuDurumu== TAMAMLANDI || randevu.randevuDurumu== IPTAL_EDILDI){
            textView.visibility=View.GONE
        }else{
            textView.visibility=View.VISIBLE
        }
    }





    @JvmStatic
    @BindingAdapter("app:persRanDurGoster")
    fun persRanDurGoster(textView: TextView, personel: Personel){
        if (personel.personelRandDur==true){
            textView.setTextColor(Color.parseColor("#068176"))
            textView.setText("Randevuya Açık")
        }else{
            textView.setTextColor(Color.parseColor("#A81207"))
            textView.setText("Randevuya Kapalı")
        }
    }

    @JvmStatic
    @BindingAdapter("app:persHesapDurGoster")
    fun persHesapDurGoster(textView: TextView, personel: Personel){
        if (personel.personelHesap==true || personel.personelYetki==true){
            textView.setTextColor(Color.parseColor("#068176"))
            textView.setText("Hesabı Var")
        }else{
            textView.setTextColor(Color.parseColor("#A81207"))
            textView.setText("Hesabı Yok")
        }
    }

    @JvmStatic
    @BindingAdapter("app:mustRanDurumu")
    fun mustRanDurumu(textView: TextView, musteriRandevu: Randevu){
        if (musteriRandevu.randevuDurumu== TAMAMLANDI){
            textView.setTextColor(Color.parseColor("#068176"))
            textView.setText("Tamamlandı")
        }else if(musteriRandevu.randevuDurumu== IPTAL_EDILDI){
            textView.setTextColor(Color.parseColor("#A81207"))
            textView.setText("İptal Edildi")
        }else{
            textView.setText("")
        }
    }

    @JvmStatic
    @BindingAdapter("app:persRanDurumu")
    fun persRanDurumu(textView: TextView, personelRandevu: Randevu){
        if (personelRandevu.randevuDurumu== TAMAMLANDI){
            textView.setTextColor(Color.parseColor("#068176"))
            textView.setText("Tamamlandı")
        }else if(personelRandevu.randevuDurumu== IPTAL_EDILDI){
            textView.setTextColor(Color.parseColor("#A81207"))
            textView.setText("İptal Edildi")
        }else{
            textView.setText("")
        }
    }




    @JvmStatic
    @BindingAdapter("app:persYetkiGoster")
    fun persYetkiGoster(textView: TextView, personel: Personel){
        if (personel.personelYetki==true ){
            textView.setTextColor(Color.parseColor("#A81207"))
            textView.setText("Yönetici")
        }else{
            textView.setTextColor(Color.parseColor("#068176"))
            textView.setText("Personel")
        }
    }

    @JvmStatic
    @BindingAdapter("app:gunSaatDuzPersYetki")
    fun gunSaatDuzPersYetki(textView: TextView, personel: Personel){
        if (personel.personelYetki  == true){
            textView.setText("PERSONEL (Yönetici)")

        }else{
            textView.setText("PERSONEL")
        }
    }

    @JvmStatic
    @BindingAdapter("app:randevuGelir")
    fun randevuGelir(textView: TextView, randevu: Randevu){
        if (randevu.randevuDurumu == TAMAMLANDI){
            val gelir=randevu.randevuGeliri.toString()
            textView.setText(gelir)

        }else{
            textView.setText("0")
        }
    }

    @JvmStatic
    @BindingAdapter("app:randevuVeresiyeTextGoster")
    fun randevuVeresiyeTextGoster(textView: TextView, randevu: Randevu){
        if (randevu.randevuGelirTuru  == VERESIYE){
            textView.visibility=View.VISIBLE
            textView.setText("Veresiye geçildi")
            textView.setTextColor(Color.parseColor("#A81207"))
        }else{
            textView.visibility=View.GONE
        }
    }

    

}