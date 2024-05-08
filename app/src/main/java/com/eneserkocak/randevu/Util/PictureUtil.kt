package com.eneserkocak.randevu.Util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.eneserkocak.randevu.model.Musteri
import com.eneserkocak.randevu.model.Personel
import com.google.firebase.storage.FirebaseStorage

object PictureUtil {


    //FİRMA LOGO EKLE
    fun firmaGorselIndir(context: Context, imageView: ImageView){
        val firmaKodu=UserUtil.firmaKodu
        val imageName= "${firmaKodu}.jpg"
        // GÖRSELLERİ STORAGE DAN ÇEK
        val storageRef = FirebaseStorage.getInstance().reference.child("Firma_image").child(imageName)
        storageRef.downloadUrl.addOnSuccessListener {
            Glide.with(context)
                .load(it)
                .transform(CircleCrop())
                .into(imageView)
        }
            .addOnFailureListener { e->
                println(e)

            }
    }
    //MUSTERİ GÖRSEL EKLE
    fun gorselIndir(musteri: Musteri, context: Context, imageView: ImageView){
        val imageName= "${UserUtil.firmaKodu}-${musteri.musteriId}.jpg"
        // GÖRSELLERİ STORAGE DAN ÇEK
        val storageRef = FirebaseStorage.getInstance().reference.child("Musteri_images").child(imageName)
        storageRef.downloadUrl.addOnSuccessListener {
            Glide.with(context)
                .load(it)
                .transform(CenterCrop(), RoundedCorners(6))
                .into(imageView)
        }
            .addOnFailureListener { e->
                println(e)

            }
    }
    //MUSTERİ GÖRSEL SİL

    fun musterigorseliSil(musteri: Musteri, context: Context, isSuccees:(Boolean)->Unit){
        val imageName= "${UserUtil.firmaKodu}-${musteri.musteriId}.jpg"
        // GÖRSELLERİ SİL
        FirebaseStorage.getInstance().reference.child("Musteri_images").child(imageName)
            .delete().addOnSuccessListener {
                isSuccees.invoke(true)
            }
            .addOnFailureListener { e->
                e.printStackTrace()
                isSuccees.invoke(false)

            }
    }



    //PERSONEL GÖRSEL EKLE

    fun gorseliAl(personel: Personel, context: Context, imageView: ImageView){
        val imageName= "${UserUtil.firmaKodu}-${personel.personelId}.jpg"
        // GÖRSELLERİ STORAGE DAN ÇEK
        val storageRef = FirebaseStorage.getInstance().reference.child("Personel_images").child(imageName)
        storageRef.downloadUrl.addOnSuccessListener {


            Glide.with(context)
                .load(it)
                .transform(CircleCrop())
                .into(imageView)
        }
            .addOnFailureListener { e->
                println(e)

            }
    }

    //PERSONEL GÖRSEL SİL

    fun gorseliSil(personel: Personel, context: Context, isSuccees:(Boolean)->Unit){
        val imageName= "${UserUtil.firmaKodu}-${personel.personelId}.jpg"
        // GÖRSELLERİ SİL
        FirebaseStorage.getInstance().reference.child("Personel_images").child(imageName)
            .delete().addOnSuccessListener {
                isSuccees.invoke(true)
            }
            .addOnFailureListener { e->
                e.printStackTrace()
                isSuccees.invoke(false)

            }
    }

    //ALACAKLAR BORÇLU MUSTERİ GÖRSEL EKLE
    fun alacaklarGorselIndir(musteri: Musteri, context: Context, imageView: ImageView){
        val imageName= "${UserUtil.firmaKodu}-${musteri.musteriId}.jpg"
        // GÖRSELLERİ STORAGE DAN ÇEK
        val storageRef = FirebaseStorage.getInstance().reference.child("Musteri_images").child(imageName)
        storageRef.downloadUrl.addOnSuccessListener {
            Glide.with(context)
                .load(it)
                .transform(CircleCrop())
                .into(imageView)
        }
            .addOnFailureListener { e->
                println(e)

            }
    }


}