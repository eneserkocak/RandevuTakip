package com.eneserkocak.randevu.view_ayarlar.borclular

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.UserUtil
import com.eneserkocak.randevu.adapter.BorclularAdapter
import com.eneserkocak.randevu.databinding.FragmentBorclularBinding
import com.eneserkocak.randevu.model.*
import com.eneserkocak.randevu.view.BaseFragment
import com.google.firebase.firestore.FirebaseFirestore


class BorclularFragment :BaseFragment<FragmentBorclularBinding>(R.layout.fragment_borclular) {

    var borclularListesi = listOf<Randevu>()

    lateinit var randevu: Randevu
    lateinit var musteri: Musteri
    var musteriListesi= listOf<Musteri>()

    val musteriVeresiyeListesi = mutableListOf<MusteriVeresiye>()


    val adapter= BorclularAdapter()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



       binding.musteriBorcRecycler.layoutManager= LinearLayoutManager(requireContext())
        binding.musteriBorcRecycler.adapter=adapter

        viewModel.veresiyeRandVerileriGetir()
        viewModel.veresiyeTamamlananRandevuListesi.observe(viewLifecycleOwner){
            it?.let {

               borclularListesi = it

                println("BORCLULAR :  ${borclularListesi.size}")

                var topAlacak = 0
               borclularListesi.forEach {

                    topAlacak= topAlacak+it.veresiyeTutari
               }

                binding.toplamAlacakCount.text=topAlacak.toString()

                if (it.isEmpty()){
                    //  AppUtil.longToast(requireContext(),"Seçilen tarih aralığında randevu bulunmamaktadır.")
                    return@let
                }

            getmusteriVeresiyeRapor()




            }
        }
    }

    fun getmusteriVeresiyeRapor(){



         musteriVeresiyeListesi.clear()

        println("BORCLULAR : ${musteriVeresiyeListesi.size}")

        //MUSTERİ LİSTESİNİ FİREBASEDEN ÇEK (musteri objesi -> musteriVereiyeListesini doldururken lazım)
        //MUSTERI LERDEN VERESIYESI OLANLARI GETİR...YOKSA BORCU OLMAYANLAR DA GELİYOR FİLTRELE
        FirebaseFirestore.getInstance().collection(MUSTERILER)
            .whereEqualTo(FIRMA_KODU, UserUtil.firmaKodu)
            .whereEqualTo(MUSTERI_VERESIYE,true)
            .get()
            .addOnSuccessListener {
                it?.let {
                    musteriListesi=it.toObjects(Musteri::class.java)
                    musteriListesi.forEach {
                        musteriVeresiyeListesi.add(filterMusteriVeresiye(it))
                    }
                }
            println("BORCLULAR: ${musteriListesi.size}")
                adapter.mustBorcListesiniGuncelle(musteriVeresiyeListesi)
            }
    }

    fun filterMusteriVeresiye(musteri: Musteri): MusteriVeresiye {

        val veresiyeler = borclularListesi.filter {


                randevu = it

                it.musteri.musteriId == musteri.musteriId
            }
            var borc = 0
            veresiyeler.forEach {
                borc = borc + it.veresiyeTutari
                println("randevular: ${veresiyeler.size}")
            }
            return MusteriVeresiye(musteri,veresiyeler.size,borc,randevu.randevuTime)

        }


}