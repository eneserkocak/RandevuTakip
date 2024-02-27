package com.eneserkocak.randevu.view_ayarlar.gun_ozeti

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.Util.UserUtil
import com.eneserkocak.randevu.Util.toTarih
import com.eneserkocak.randevu.adapter.GunOzetiAdapter
import com.eneserkocak.randevu.adapter.GunOzetiGiderAdapter
import com.eneserkocak.randevu.databinding.FragmentGunOzetiGiderBinding
import com.eneserkocak.randevu.model.*
import com.eneserkocak.randevu.view.BaseFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


class GunOzetiGiderFragment : BaseFragment<FragmentGunOzetiGiderBinding>(R.layout.fragment_gun_ozeti_gider) {

    lateinit var cal: Calendar
    lateinit var gider: Gider
    var giderList = listOf<Gider>()
    var filtreGiderListesi = listOf<Gider>()
    val adapter = GunOzetiGiderAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gunOzetiGiderRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.gunOzetiGiderRecycler.adapter = adapter

        cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val tarih = sdf.format(cal.time)
        binding.dateText.setText(tarih)

       /* println("GUN OZETİ UID: ${UserUtil.uid}")
        println("GUN OZETİ FIRMA KODU: ${UserUtil.firmaKodu}")*/

        getData() {
            filtreGiderListesi = it

            adapter.giderListesiniGuncelle(filtreGiderListesi)


        }


        binding.nextDate.setOnClickListener {
            cal.add(Calendar.DAY_OF_YEAR, 1)
            binding.dateText.text = sdf.format(cal.time)

            getData() {
                filtreGiderListesi = it

                adapter.giderListesiniGuncelle(filtreGiderListesi)


            }

        }
        binding.backDate.setOnClickListener {
            cal.add(Calendar.DAY_OF_YEAR, -1)
            binding.dateText.text = sdf.format(cal.time)

            getData() {
                filtreGiderListesi = it

                adapter.giderListesiniGuncelle(filtreGiderListesi)

            }
        }


    }

    fun getData(giderler: (List<Gider>) -> Unit) {

        FirebaseFirestore.getInstance().collection(GIDERLER)
            .whereEqualTo(FIRMA_KODU,UserUtil.firmaKodu)
            .get().addOnSuccessListener {
            it?.let {
                val giderList = it.toObjects(Gider::class.java)

                giderList.forEach {
                    it?.let {
                        gider = it

                        filtreGiderListesi = giderList.filter {
                            it.giderTarih.toDate().toTarih() == cal.time.toTarih()
                        }

                        if (filtreGiderListesi.isEmpty()) {
                            binding.toplamGiderCount.setText("0")
                        } else {
                            var giderTutar = 0
                            filtreGiderListesi.forEach {
                                giderTutar = giderTutar + it.giderTutar
                                binding.toplamGiderCount.text = giderTutar.toString()
                            }
                        }
                    }
                }
                giderler.invoke(filtreGiderListesi)
            }
            //println()
        }
            .addOnFailureListener {
                println(it)
            }


    }


}