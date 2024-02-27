package com.eneserkocak.randevu.Util

import android.Manifest
import android.app.Activity
import android.content.ContentProvider
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

import com.eneserkocak.randevu.model.*
import com.eneserkocak.randevu.view.MainActivity
import com.eneserkocak.randevu.viewModel.AppViewModel
import com.google.android.gms.common.util.SharedPreferencesUtils
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.oAuthCredential
import com.google.firebase.auth.ktx.oAuthProvider
import com.google.firebase.firestore.FirebaseFirestore


import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.coroutineContext

object AppUtil {

    //Toolbar app:titleTextColor="#081C34"

    //var kullanicilar= "wGdMgWOziBcEGjeRZpGeDGcOkQg1"
   // var firmaKodu= 100

    fun  randevuQuery() = FirebaseFirestore.getInstance().collection(RANDEVULAR)
        .whereEqualTo(FIRMA_KODU,UserUtil.firmaKodu)


   /* fun randevuDocumentPath(randevu: Randevu): String{
        val sdf = SimpleDateFormat("yyyyMMddHHmm")
        val _randevuTime = sdf.format(randevu.randevuTime.toDate())
        val documentPath = randevu.firmaKodu.toString()+"-"+randevu.personel.personelId.toString()+"-"+_randevuTime
        return documentPath

    }*/

    fun randevuDocumentPath(randevu: Randevu): String{
        val sdf = SimpleDateFormat("yyyyMMddHHmm")
        val _randevuTime = sdf.format(randevu.randevuTime.toDate())
        val documentPath = randevu.firmaKodu.toString()+"-"+randevu.personel.personelId.toString()+"-"+randevu.musteri.musteriId.toString()+"-"+_randevuTime
        return documentPath

    }



    fun defaultCalismaGunListesi(): List<CalismaGun> {
        val calismaGunList= mutableListOf<CalismaGun>()
            Gunler.values().forEach {
            calismaGunList.add(CalismaGun(it.int))}
        return calismaGunList.toList()
    }


             //FİREBASE AUTH FİRMA EKLE VE DÜZENLE

    fun firmaDocumentPath(firma: Firma) : String{
        val documentPath = firma.firmaKodu.toString()
        return documentPath
    }


    fun firmalarKaydet(firma: Firma,isSuccees:(Boolean)->Unit ){

        val firmalarMap = mapOf<String,Any>(
            //KULLANICILAR to firma.kullanicilar!!,
            FIRMA_KODU to firma.firmaKodu,
            EMAIL to firma.email,
            SIFRE to firma.sifre,
            FIRMA_ADI to firma.firmaAdi,
            FIRMA_TEL to firma.firmaTel,
            FIRMA_ADRES to firma.firmaAdres


        )
        val documentPath = firmaDocumentPath(firma)
        FirebaseFirestore.getInstance().collection(FIRMALAR).document(documentPath)
            .set(firmalarMap)
            .addOnSuccessListener {
                isSuccees.invoke(true)
            }
            .addOnFailureListener {
                it.printStackTrace()
                isSuccees.invoke(false)
            }

    }

                             //FİREBASE AUTH FİRMA EKLE VE DÜZENLE SONU





    //FİREBASE PERSONEL EKLE, SİL VE DÜZENLE
    fun documentPath(personel: Personel) : String{
        val documentPath = personel.firmaKodu.toString()+"-"+personel.personelId.toString()
        return documentPath
    }

      fun personelKaydet(personel: Personel,isSuccees:(Boolean)->Unit ){

            val personelMap = mapOf<String,Any>(
            FIRMA_KODU to personel.firmaKodu,
            PERSONEL_ID to personel.personelId,
            PERSONEL_ADI to personel.personelAdi,
            PERSONEL_TEL to personel.personelTel,
            PERSONEL_MAIL to personel.personelMail,
            PERSONEL_UNVAN to personel.personelUnvan,
            PERSONEL_YETKI to personel.personelYetki,
            PERSONEL_HESAP to personel.personelHesap,
            PERSONEL_RANDDUR to personel.personelRandDur,
            PERSONEL_CALISMA_GUN to personel.personelCalismaGun,
            PERSONEL_CALISMA_DAKIKA to personel.personelCalismaDakika,



        )
        val documentPath = documentPath(personel)
        FirebaseFirestore.getInstance().collection(PERSONELLER).document(documentPath).set(personelMap)
            .addOnSuccessListener {
                isSuccees.invoke(true)
            }
            .addOnFailureListener {
                it.printStackTrace()
                isSuccees.invoke(false)
            }

    }
    fun personelSil(personel: Personel, isSuccees:(Boolean)->Unit ){

        val documentPath = documentPath(personel)
        FirebaseFirestore.getInstance().collection(PERSONELLER).document(documentPath).delete()
            .addOnSuccessListener {
                isSuccees.invoke(true)
            }
            .addOnFailureListener {
                it.printStackTrace()
                isSuccees.invoke(false)
            }
    }
                                              // PERSONEL KODLARIN SONU/////////

    //MÜŞTERİ EKLE,SİL VE DÜZENLE :
    fun documentPath(musteri: Musteri) : String{
        val documentPath = musteri.firmaKodu.toString()+"-"+musteri.musteriId.toString()
        return documentPath
    }

    fun musteriKaydet(musteri: Musteri,isSuccees:(Boolean)->Unit ){
        val musteriMap = mapOf<String,Any>(
            FIRMA_KODU to musteri.firmaKodu,
            MUSTERI_ID to musteri.musteriId,
            MUSTERI_ADI to musteri.musteriAdi,
            MUSTERI_TEL to musteri.musteriTel,
            MUSTERI_MAIL to musteri.musteriMail,
            MUSTERI_ADRES to musteri.musteriAdres,
            MUSTERI_NOT to musteri.musteriNot,
            MUSTERI_GORSEL to musteri.musteriGorsel,
           MUSTERI_VERESIYE to musteri.musteriVeresiye,
            MUSTERI_BORC to musteri.musteriBorc

        )
        val documentPath = documentPath(musteri)
        FirebaseFirestore.getInstance().collection(MUSTERILER).document(documentPath).set(musteriMap)
            .addOnSuccessListener { isSuccees.invoke(true) }
            .addOnFailureListener { isSuccees.invoke(false) }

    }
    fun musteriSil(musteri: Musteri, isSuccees:(Boolean)->Unit ){

        val documentPath = documentPath(musteri)
        FirebaseFirestore.getInstance().collection(MUSTERILER).document(documentPath).delete()
            .addOnSuccessListener {
                isSuccees.invoke(true)
            }
            .addOnFailureListener {
                it.printStackTrace()
                isSuccees.invoke(false)
            }
    }
                                          // MUSTERİ EKLE VE DUZENLE KODLARIN SONU/////////


    //HİZMET EKLE, SİL VE DÜZENLE :
    fun documentPath(hizmet: Hizmet) : String{
        val documentPath = hizmet.firmaKodu.toString()+"-"+hizmet.hizmetId.toString()
        return documentPath
    }

    fun hizmetKaydet(hizmet: Hizmet,isSuccees:(Boolean)->Unit ){
        val hizmetMap = mapOf<String,Any>(
            FIRMA_KODU to hizmet.firmaKodu,
            HIZMET_ID to hizmet.hizmetId,
            HIZMET_ADI to hizmet.hizmetAdi,
            ACIKLAMA to hizmet.aciklama,
            FIYAT to hizmet.fiyat,
            HIZMET_GOR_DUR to hizmet.hizmetGorDur


        )

        val documentPath = documentPath(hizmet)
        FirebaseFirestore.getInstance().collection(HIZMETLER).document(documentPath).set(hizmetMap)
            .addOnSuccessListener {
                isSuccees.invoke(true)
            }
            .addOnFailureListener {
                it.printStackTrace()
                isSuccees.invoke(false)
            }

    }
    fun hizmetSil(hizmet: Hizmet, isSuccees:(Boolean)->Unit ){

        val documentPath = documentPath(hizmet)
        FirebaseFirestore.getInstance().collection(HIZMETLER).document(documentPath).delete()
            .addOnSuccessListener {
                isSuccees.invoke(true)
            }
            .addOnFailureListener {
                it.printStackTrace()
                isSuccees.invoke(false)
            }
    }
                                           // HİZMET EKLE VE DUZENLE KODLARIN SONU/////////


    //GİDER EKLE, SİL VE DÜZENLE :
    fun documentPath(gider: Gider) : String{
        val documentPath = gider.firmaKodu.toString()+"-"+gider.giderId.toString()
        return documentPath
    }

    fun giderKaydet(gider: Gider,isSuccees:(Boolean)->Unit ){
        val giderMap = mapOf<String,Any>(
            FIRMA_KODU to gider.firmaKodu,
            GIDER_ID to gider.giderId,
            GIDER_ADI to gider.giderAdi,
            GIDER_TUTAR to gider.giderTutar,
            GIDER_TARİH to gider.giderTarih,

            )
        val documentPath = documentPath(gider)
        FirebaseFirestore.getInstance().collection(GIDERLER).document(documentPath).set(giderMap)
            .addOnSuccessListener {
                isSuccees.invoke(true)
            }.addOnFailureListener {
                it.printStackTrace()
                isSuccees.invoke(false)
            }

    }
    fun giderSil(gider: Gider, isSuccees:(Boolean)->Unit ){

        val documentPath = documentPath(gider)
        FirebaseFirestore.getInstance().collection(GIDERLER).document(documentPath).delete()
            .addOnSuccessListener {
                isSuccees.invoke(true)
            }
            .addOnFailureListener {
                it.printStackTrace()
                isSuccees.invoke(false)
            }
    }

    // GİDER EKLE VE DUZENLE KODLARIN SONU/////////





    fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    }



    fun longToast(context: Context?,message:String){
        context?.let {
            Toast.makeText(it,message,Toast.LENGTH_LONG).show()
        }

    }
}