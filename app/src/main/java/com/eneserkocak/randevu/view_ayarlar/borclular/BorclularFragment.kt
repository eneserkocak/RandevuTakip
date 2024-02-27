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



    lateinit var adapter:BorclularAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter=BorclularAdapter()

       binding.musteriBorcRecycler.layoutManager= LinearLayoutManager(requireContext())
        binding.musteriBorcRecycler.adapter=adapter



        viewModel.veresiyeRandVerileriGetir()
        viewModel.veresiyeTamamlananRandevuListesi.observe(viewLifecycleOwner){
            it?.let {



                var topAlacak = 0
              it.forEach {

                    topAlacak= topAlacak+it.veresiyeTutari
               }

                binding.toplamAlacakCount.text=topAlacak.toString()

                if (it.isEmpty()){
                    //  AppUtil.longToast(requireContext(),"Seçilen tarih aralığında randevu bulunmamaktadır.")
                    return@let
                }

            getmusteriVeresiyeRapor(it){
                adapter.mustBorcListesiniGuncelle(it)
            }




            }
        }
    }

    fun getmusteriVeresiyeRapor(randevus: List<Randevu>,callback:(List<MusteriVeresiye>)->Unit) {




        //MUSTERİ LİSTESİNİ FİREBASEDEN ÇEK (musteri objesi -> musteriVereiyeListesini doldururken lazım)
        //MUSTERI LERDEN VERESIYESI OLANLARI GETİR...YOKSA BORCU OLMAYANLAR DA GELİYOR FİLTRELE
        FirebaseFirestore.getInstance().collection(MUSTERILER)
            .whereEqualTo(FIRMA_KODU, UserUtil.firmaKodu)
            .whereEqualTo(MUSTERI_VERESIYE,true)
            .get()
            .addOnSuccessListener {
                it?.let {
                   val  musteriListesi=it.toObjects(Musteri::class.java)
                    val musteriVeresiyeListesi = mutableListOf<MusteriVeresiye>()
                    musteriListesi.forEach {
                        musteriVeresiyeListesi.add(filterMusteriVeresiye(it,randevus))
                    }

                    val list=musteriVeresiyeListesi.sortedBy {
                        it.musteri.musteriAdi
                    }

                    callback.invoke(list)
                }
            }
    }

    fun filterMusteriVeresiye(musteri: Musteri, borclularListesi: List<Randevu>): MusteriVeresiye {

        val musterininVeresiyeRandevulari = borclularListesi.filter { it.musteri.musteriId == musteri.musteriId }
            var borc = 0
           musterininVeresiyeRandevulari.forEach {
                borc = borc + it.veresiyeTutari
           }
            return MusteriVeresiye(musteri,musterininVeresiyeRandevulari.size,borc)

        }


}