package com.eneserkocak.randevu.view_ayarlar.raporlar

import android.os.Bundle
import android.view.View
import androidx.core.util.Pair
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.Util.UserUtil
import com.eneserkocak.randevu.Util.toTarih
import com.eneserkocak.randevu.Util.toTimestamp
import com.eneserkocak.randevu.adapter.RaporlarAdapter
import com.eneserkocak.randevu.databinding.FragmentRaporlarBinding
import com.eneserkocak.randevu.model.*

import com.eneserkocak.randevu.view.BaseFragment
import com.eneserkocak.randevu.view_musteri.filtreRandevuListesi

import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.measureTime

//create an extension function on a date class which returns a string
private fun Date.dateToText(format: String): String {
    val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
    return dateFormatter.format(this)
}

class RaporlarFragment: BaseFragment<FragmentRaporlarBinding>(R.layout.fragment_raporlar) {

    var personelListesi= listOf<Personel>()
    lateinit var randevu:Randevu
    lateinit var personel: Personel

    var raporRandevuListesi = listOf<Randevu>()
    lateinit var adapter: RaporlarAdapter

    val personelRaporListesi = mutableListOf<PersonelRandevu>()

    lateinit var gider: Gider
    var filtreGiderListesi = listOf<Gider>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter=RaporlarAdapter()

        binding.raporlarRecycler.layoutManager=LinearLayoutManager(requireContext())
        binding.raporlarRecycler.adapter=adapter

 //İlk girişte "0" a eşitle, Yoksa ilk girişte hepsi "0" görünüor FAKAT gider "Tarih text view alt old. için "HİÇ GÖRÜNMÜYOR
        binding.toplamGiderCount.setText("0")

        binding.raporlarTarihTextView.setOnClickListener {

            val picker = MaterialDatePicker.Builder.dateRangePicker()

                .setTitleText("Tarih aralığı seçiniz..!")
                .setSelection(Pair(null, null))
                .build()

            picker.show(parentFragmentManager, "TAG")
            picker.addOnPositiveButtonClickListener {


  // ViewModel dan secili tarih aralığını çekerken "whereLessThanEqualTo" equal ÇALIŞMADI.
  // 2.SEÇİLENN TARİHİN BİR GÜN SONRASINI ALMAM GEREKİYOR: YANİ 1-30 OCAK görüntülemek için, 2.tarih te  31 OCAK I çağıracam.
                val cal=Calendar.getInstance()
                cal.time=it.second.toTimestamp().toDate()
                cal.add(Calendar.DAY_OF_YEAR,1)
                val birGunSonra=cal.time
                val second=birGunSonra.time

                //VİEW MODEL DAN TÜM RANDEVULARI SEÇİLEN TARİH ARALIĞINI FİLTRELEYEREK BURDA AL
                //   viewModel.raporRandVerileriGetir(it.first.toTimestamp(),it.second.toTimestamp()) 2.tarihi bi önceki gün alıyor İPTAL
                viewModel.raporRandVerileriGetir(it.first.toTimestamp(),second.toTimestamp())


                //AŞAĞIDAKİ FİLTREYE GEREK KALMADI..TÜM RAND LARI (randRaporVerileriniGetir)
                // yukarda ve VieModelda filtreleyerek seçilen tarihi getiriyorum..

                /*val filterList = personelRaporListesi.filter {randevu->
                      randevu.randevuTime>=it.first.toTimestamp()&&randevu.randevuTime<=it.second.toTimestamp()
                 }*/


   //GİDER LİSTESİNİ BURADA AŞAĞIDA OLuŞTURULAN FONKSİYONDAN AL
              getData(it.first.toTimestamp(),second.toTimestamp()) {

                    filtreGiderListesi = it

                  if (filtreGiderListesi.isEmpty()){
                      binding.toplamGiderCount.setText("0")
                  }else{
                      var giderTutar=0
                      filtreGiderListesi.forEach {
                          giderTutar=giderTutar+it.giderTutar
                          binding.toplamGiderCount.setText(giderTutar.toString())
                      }
                  }
              }

                val date1Text=convertTimeToDate(it.first)
                println("date1Text:${date1Text}")
                val date2Text=convertTimeToDate(it.second)
                println("date2Text:${date2Text}")

                binding.raporlarTarihTextView.setText(date1Text + "-" + date2Text)

            }
            picker.addOnNegativeButtonClickListener {
                picker.dismiss()
            }


    }

        viewModel.raporRandevuListesi.observe(viewLifecycleOwner) {
            it?.let {
                //  raporRandevuListesi = it  -> AŞAĞIDA GELİR HESAPLA FONK İÇİNDE

                if (it.isEmpty()) {
                    binding.raporlarRecycler.visibility = View.GONE
                    //  AppUtil.longToast(requireContext(),"Seçilen tarih aralığında randevu bulunmamaktadır.")
                } else{ binding.raporlarRecycler.visibility=View.VISIBLE}

                var topGelir = geliriHesapla(it)

                binding.toplamGelirCount.text=topGelir.toString()
                binding.toplamRandCount.text=raporRandevuListesi.size.toString()


//İlk kez seçilen aralıkta randevu yoksa (BOŞSA) filterRandevuPersonel DE FOR EACH VE FILTER da ÇÖKÜYOR.
// Bu nu engellemek için burda RETURN yap

                if (it.isEmpty()){
                    //  AppUtil.longToast(requireContext(),"Seçilen tarih aralığında randevu bulunmamaktadır.")
                    return@let
                }

                getPersonelRandevuRapor()
                //adapter.persRandListesiniGuncelle(raporRandevuListesi)
            }
        }


 }

    private fun geliriHesapla(it: List<Randevu>): Int {
        var topGelir = 0
        raporRandevuListesi = it
        raporRandevuListesi.forEach {
            topGelir = topGelir + it.randevuGeliri
        }
        return topGelir
    }

    fun getPersonelRandevuRapor(){
       //Her Tarih seçiminde bu fonk çalışıyor.Aşağıda CLEAR yapmazsak eski listeyi silmeden. Altına tekrar liste oluşturuyor.
        personelRaporListesi.clear()
       //PERSONEL LİSTESİNİ FİREBASEDEN ÇEK (personel objesi -> personelRaporListesini doldururken lazım)
        FirebaseFirestore.getInstance().collection(PERSONELLER)
            .whereEqualTo(FIRMA_KODU,UserUtil.firmaKodu)
            .get()
            .addOnSuccessListener {
                it?.let {
                    personelListesi=it.toObjects(Personel::class.java)
                    personelListesi.forEach {
                        personelRaporListesi.add(filterRandevuPersonel(it))
                    }
                }
                    val list=personelRaporListesi.sortedBy {
                        it.personel.personelAdi
                    }

                    adapter.persRandListesiniGuncelle(list)

            }
    }

    fun filterRandevuPersonel(personel: Personel):PersonelRandevu {

            val randevular =  raporRandevuListesi.filter {
                   randevu=it

        it.personel.personelId==personel.personelId }
        var gelir = 0
            randevular.forEach {
                 gelir = gelir+ it.randevuGeliri
     //   println("randevular: ${randevular.size}")
          }
        return PersonelRandevu(personel,randevular.size,gelir,randevu.randevuTime)

    }


    private fun convertTimeToDate(time: Long): String {
            val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            utc.timeInMillis = time
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return format.format(utc.time)
        }

    //GİDER LİSTESİNİ GETİR
    fun getData(tarih1: com.google.firebase.Timestamp, tarih2: com.google.firebase.Timestamp, giderler: (List<Gider>) -> Unit) {

        FirebaseFirestore.getInstance().collection(GIDERLER)
            .whereEqualTo(FIRMA_KODU,UserUtil.firmaKodu)
            .whereGreaterThanOrEqualTo(GIDER_TARİH,tarih1)
            .whereLessThanOrEqualTo(GIDER_TARİH,tarih2)


            .get().addOnSuccessListener {
            it?.let {
                val giderList = it.toObjects(Gider::class.java)

               // println("GİDERLER MERAK KONUSU: ${giderList}")
                giderler.invoke(giderList)
            }

        }
            .addOnFailureListener {
                println(it)
            }


    }


    }

