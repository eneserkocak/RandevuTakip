package com.eneserkocak.randevu.Util

import android.Manifest
import android.app.Activity
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

import com.eneserkocak.randevu.model.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

object AppUtil {

                //android:textColorHint="#5E5858"

    var firmaId = 1
    fun  randevuQuery() = FirebaseFirestore.getInstance().collection(RANDEVULAR)
        .whereEqualTo(FIRMA_ID,firmaId)


    fun randevuDocumentPath(randevu: Randevu): String{
        val sdf = SimpleDateFormat("yyyyMMddHHmm")
        val _randevuTime = sdf.format(randevu.randevuTime.toDate())
        val documentPath = randevu.firmaId.toString()+"-"+randevu.personel.personelId.toString()+"-"+_randevuTime
        return documentPath

    }



    fun defaultCalismaGunListesi(): List<CalismaGun> {
        val calismaGunList= mutableListOf<CalismaGun>()
            Gunler.values().forEach {
            calismaGunList.add(CalismaGun(it.int))}
        return calismaGunList.toList()
    }



    //FİREBASE PERSONEL EKLE, SİL VE DÜZENLE
    fun documentPath(personel: Personel) : String{
        val documentPath = personel.firmaId.toString()+"-"+personel.personelId.toString()
        return documentPath
    }

      fun personelKaydet(personel: Personel,isSuccees:(Boolean)->Unit ){

            val personelMap = mapOf<String,Any>(
            FIRMA_ID to personel.firmaId,
            PERSONEL_ID to personel.personelId,
            PERSONEL_ADI to personel.personelAdi,
            PERSONEL_TEL to personel.personelTel,
            PERSONEL_MAIL to personel.personelMail,
            PERSONEL_UNVAN to personel.personelUnvan,
            PERSONEL_RANDDUR to personel.personelRandDur,
            PERSONEL_CALISMA_GUN to personel.personelCalismaGun,
            PERSONEL_CALISMA_DAKIKA to personel.personelCalismaDakika,
           // PERSONEL_GORSEL to personel.personelGorsel,

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
        val documentPath = musteri.firmaId.toString()+"-"+musteri.musteriId.toString()
        return documentPath
    }

    fun musteriKaydet(musteri: Musteri,isSuccees:(Boolean)->Unit ){
        val musteriMap = mapOf<String,Any>(
            FIRMA_ID to musteri.firmaId,
            MUSTERI_ID to musteri.musteriId,
            MUSTERI_ADI to musteri.musteriAdi,
            MUSTERI_TEL to musteri.musteriTel,
            MUSTERI_MAIL to musteri.musteriMail,
            MUSTERI_ADRES to musteri.musteriAdres,
            MUSTERI_NOT to musteri.musteriNot,
            MUSTERI_GORSEL to musteri.musteriGorsel

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
        val documentPath = hizmet.firmaId.toString()+"-"+hizmet.hizmetId.toString()
        return documentPath
    }

    fun hizmetKaydet(hizmet: Hizmet,isSuccees:(Boolean)->Unit ){
        val hizmetMap = mapOf<String,Any>(
            FIRMA_ID to hizmet.firmaId,
            HIZMET_ID to hizmet.hizmetId,
            HIZMET_ADI to hizmet.hizmetAdi,
            ACIKLAMA to hizmet.aciklama,
            FIYAT to hizmet.fiyat,

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
        val documentPath = gider.firmaId.toString()+"-"+gider.giderId.toString()
        return documentPath
    }

    fun giderKaydet(gider: Gider,isSuccees:(Boolean)->Unit ){
        val giderMap = mapOf<String,Any>(
            FIRMA_ID to gider.firmaId,
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