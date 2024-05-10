package com.eneserkocak.randevu.view

import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.Util.UserUtil
import com.eneserkocak.randevu.databinding.DialogCallerIdBinding
import com.eneserkocak.randevu.databinding.DialogCallerMusteriEkleBinding
import com.eneserkocak.randevu.model.*
import com.eneserkocak.randevu.view_musteri.YeniMusteriEkleFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class CallerActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  val binding= ActivityCallerBinding.inflate(layoutInflater)
      //  setContentView(binding.root)

        val phoneNum= intent.extras?.getString("number")

        val aranacakNo= phoneNum?.subSequence(2,13).toString()
        val sharedPreferences = AppUtil.getSharedPreferences(this)
        sharedPreferences.edit().putString(MUSTERI_NO,aranacakNo).apply()




        val binding = DialogCallerIdBinding.inflate(LayoutInflater.from(this))


        if (phoneNum != null){
            val arayanNumara=phoneNum.subSequence(2,13).toString()
            binding.musteriTelGostText.setText(arayanNumara)

        }else{
            Toast.makeText(this, "Telefon numarası alınamadı", Toast.LENGTH_LONG).show()
        }

        val sharedPref = AppUtil.getSharedPreferences(this)
        val firmaKoduAl = sharedPref.getInt(FIRMA_KODU, 0)
        UserUtil.firmaKodu =firmaKoduAl

        val gelenNumara = phoneNum?.subSequence(2, 13).toString()
        FirebaseFirestore.getInstance().collection(MUSTERILER)
            .whereEqualTo(FIRMA_KODU, UserUtil.firmaKodu)
            .get()
            .addOnSuccessListener {
                it?.let {
                    val mustList = it.toObjects(Musteri::class.java)


                    var arayanMustList = listOf<Musteri>()
                    arayanMustList = mustList.filter {
                        it?.let {
                            it.musteriTel == gelenNumara
                        }!!
                    }
                    if (arayanMustList.isNotEmpty()){
                        val arayanMusteri= arayanMustList.get(0)
                        val arayanMusteriAdi= arayanMusteri.musteriAdi.uppercase()
                        binding.musteriAdiText.setText(arayanMusteriAdi)
                    }else{
                        binding.musteriAdiText.setText("KAYIT YOK")
                       // binding.musteriAdiText.setTextColor(Color.parseColor("#A81207"))
                    }
                }
            }


        val dialog = AlertDialog.Builder(this@CallerActivity).setView(binding.root).show()


        binding.musteriEkleBtn.setOnClickListener {

            dialog.dismiss()
            val binding = DialogCallerMusteriEkleBinding.inflate(LayoutInflater.from(this))
            val musteriDialog = AlertDialog.Builder(this@CallerActivity).setView(binding.root).show()

            val sharedPref = AppUtil.getSharedPreferences(this)
            val musteriNo = sharedPref.getString(MUSTERI_NO, "bos")

            binding.musteriTelText.setText(musteriNo.toString())


            binding.kaydetBtn.setOnClickListener {


                val mustIsim = binding.musteriIsmiText.text.toString().toLowerCase()
                val mustTel = musteriNo.toString().filterNot { it.isWhitespace() }

                if (mustIsim.isEmpty()) {
                    Toast.makeText(this@CallerActivity,"İsim girilmeden müşteri eklenemez.!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                FirebaseFirestore.getInstance().collection(MUSTERILER)
                    .whereEqualTo(FIRMA_KODU,UserUtil.firmaKodu)
                    .orderBy(MUSTERI_ID, Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .addOnSuccessListener {
                        it?.let {
                            var yeniMusteriId=0
                            if (it.isEmpty) yeniMusteriId=1
                            else{
                                for (document in it) {
                                    val sonMusteri = document.toObject(Musteri::class.java)
                                    yeniMusteriId = sonMusteri.musteriId+1

                                }
                            }
                            val yeniMusteri= Musteri(UserUtil.firmaKodu,yeniMusteriId,mustIsim,mustTel)
                            musteriEkle(yeniMusteri)


                        }
                    }.addOnFailureListener {
                        it.printStackTrace()
                    }

                    musteriDialog.dismiss()
            }

            binding.vazgecBtn.setOnClickListener {
                musteriDialog.dismiss()
            }


        }

        binding.whastsAppBtn.setOnClickListener {

            val no=phoneNum?.subSequence(2,13).toString()
            val telNo= "+90${no}"

                    if(telNo.length==14){
                        val url = "https://api.whatsapp.com/send?phone=$telNo"
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse(url)
                        startActivity(i)

                        AppUtil.shortToast(this,"Müşteriyle whatsapp ile iletişime geçiliyor..!")
                    }else{
                        AppUtil.shortToast(this,"İletişime geçilemedi..!")
                    }

        }
        binding.mustAraBtn.setOnClickListener {
            musteriAraDialog()
        }

        binding.kapatBtn.setOnClickListener {
            dialog.dismiss()

        }

    }

    fun musteriAraDialog(){

        val sharedPref = AppUtil.getSharedPreferences(this)
        val musteriNo = sharedPref.getString(MUSTERI_NO, "bos")

        val alert = AlertDialog.Builder(this)
        alert.setMessage("Numara geri aransın mı?")

        alert.setPositiveButton("EVET", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {

                var musteriTel= musteriNo
                if (musteriTel!!.isNotEmpty()){
                    val callIntent= Intent(Intent.ACTION_CALL)
                    callIntent.data= Uri.parse("tel:$musteriTel")
                    startActivity(callIntent)
                }else{
                    Toast.makeText(this@CallerActivity,"Arama yapılamıyor!..",Toast.LENGTH_LONG).show()
                }

                AppUtil.shortToast(this@CallerActivity,"Numara aranıyor..!")
                dialog?.dismiss()

            }
        })
        alert.setNegativeButton("HAYIR", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
            }
        })

        alert.show()
    }

    fun musteriEkle(musteri: Musteri) {
        AppUtil.musteriKaydet(musteri) {
            if(it) {
                Toast.makeText(this@CallerActivity, "Yeni Müşteri Eklendi, Eklenen Müşteriyi Uygulama İçerisinde Düzenleyebilirsiniz", Toast.LENGTH_SHORT).show()

            } else
                Toast.makeText(this, "HATA", Toast.LENGTH_LONG).show()
        }
    }



    /*fun musteriEkleSayfasinaGit(
        arayanNumara: String,
        firmaKodu: String?,
        context: Context
    ) {

       // dialog.dismiss()

        val sharedPref = AppUtil.getSharedPreferences(this)
        val musteriNo = sharedPref.getString(MUSTERI_NO, "bos")

        val yeniMusteriEkleFragment = YeniMusteriEkleFragment()
        val bundle = Bundle()
        bundle.putString("arayanNumara", musteriNo)
        bundle.putString("gelinenAdres", "CallerActivity")
        bundle.putString("firmaKodu", UserUtil.firmaKodu.toString())



        yeniMusteriEkleFragment.arguments = bundle
        yeniMusteriEkleFragment.show(supportFragmentManager, "yeniMusteriEkleFragment")
    }*/



 }




