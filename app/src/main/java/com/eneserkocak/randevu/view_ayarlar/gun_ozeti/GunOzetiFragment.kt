package com.eneserkocak.randevu.view_ayarlar.gun_ozeti

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.toTarih

import com.eneserkocak.randevu.adapter.GunOzetiAdapter
import com.eneserkocak.randevu.databinding.FragmentGunOzetiBinding
import com.eneserkocak.randevu.model.*
import com.eneserkocak.randevu.view.BaseFragment


import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*



class GunOzetiFragment : BaseFragment<FragmentGunOzetiBinding>(R.layout.fragment_gun_ozeti) {


    lateinit var cal: Calendar
    var tamRandevuListesi = listOf<Randevu>()
    val adapter = GunOzetiAdapter()
    var filtreRandevuListesi = listOf<Randevu>()
    var veresiyeRandListesi = listOf<Randevu>()
    var filtreVeresiyeListesi= listOf<Randevu>()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gunOzetiRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.gunOzetiRecycler.adapter = adapter


        cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val tarih = sdf.format(cal.time)
        binding.dateText.text = tarih

        println(tarih)

        randevuOzet()

        veresiyeRandevuOzet()

        binding.nextDate.setOnClickListener {
            cal.add(Calendar.DAY_OF_YEAR, 1)
            binding.dateText.text = sdf.format(cal.time)

            randevuOzet()

            veresiyeRandevuOzet()
        }
        binding.backDate.setOnClickListener {
            cal.add(Calendar.DAY_OF_YEAR, -1)
            binding.dateText.text = sdf.format(cal.time)

            randevuOzet()

            veresiyeRandevuOzet()
        }


    }
    //RANDEVU ÖZETİ VERİLERİNİ : SEÇİLİ TARİHE GÖRE GÖSTERMEK İÇİN AŞAĞIDA FONK İÇİNDE YAP-> tarih setOnClicList altlarında çağır

    fun randevuOzet(){
        viewModel.tamamlananRandVerileriGetir()
        viewModel.tamamlananRandevuListesi.observe(viewLifecycleOwner){
            it?.let {

                tamRandevuListesi = it

                tamRandevuListesi?.let {

                filtreRandevuListesi = tamRandevuListesi.filter {
                    it.randevuTime.toDate().toTarih() == cal.time.toTarih() }
                }

                if (filtreRandevuListesi.isEmpty()) {binding.toplamGelirCount.setText("0")

                }else{

                    var gelir= 0
                filtreRandevuListesi.forEach {
                    gelir=gelir+it.randevuGeliri
                    binding.toplamGelirCount.text= gelir.toString()

                }
           }


                println("RANDEVULİST:  ${tamRandevuListesi}")
                //adapter.randevuListesiniGuncelle(it)

                binding.toplamRandCount.text=filtreRandevuListesi.size.toString()
                adapter.randevuListesiniGuncelle(filtreRandevuListesi)

                val size= tamRandevuListesi.size
                println("size: ${size}")

                println("filtre: ${filtreRandevuListesi.size}")

            }
        }
    }


    fun veresiyeRandevuOzet(){
        viewModel.veresiyeRandVerileriGetir()
        viewModel.veresiyeTamamlananRandevuListesi.observe(viewLifecycleOwner){
            it?.let {

               veresiyeRandListesi = it

                veresiyeRandListesi?.let {

                    filtreVeresiyeListesi = veresiyeRandListesi.filter {
                        it.randevuTime.toDate().toTarih() == cal.time.toTarih() }
                }

                if (filtreVeresiyeListesi.isEmpty()) {binding.toplamVeresiyeCount.setText("0")

                }else{

                    var veresiye= 0
                    filtreVeresiyeListesi.forEach {
                        veresiye=veresiye+it.veresiyeTutari
                        binding.toplamVeresiyeCount.text= veresiye.toString()

                    }
                }


            }
        }
    }

}