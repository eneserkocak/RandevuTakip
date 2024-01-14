package com.eneserkocak.randevu.view_ayarlar.gun_saat_ayar

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.TimeUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.Util.TimeUtil
import com.eneserkocak.randevu.adapter.GunlerRecyclerAdapter
import com.eneserkocak.randevu.databinding.FragmentGunSaatDuzenleBinding
import com.eneserkocak.randevu.model.CalismaGun

import com.eneserkocak.randevu.model.Personel

import com.eneserkocak.randevu.view.BaseFragment


class GunSaatDuzenleFragment: BaseFragment<FragmentGunSaatDuzenleBinding>(R.layout.fragment_gun_saat_duzenle) {


     lateinit var personel:Personel

     lateinit var adapter: GunlerRecyclerAdapter

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel= viewModel
        adapter= GunlerRecyclerAdapter(parentFragmentManager)



        viewModel.secilenPersonel.observe(viewLifecycleOwner){ secilenPersonel->

            secilenPersonel?.let {

                    personel=it

                val checkedId = if (!personel.personelRandDur) R.id.gizliBtn else R.id.gorunurBtn
                binding.RadioGrup.check(checkedId)

                adapter.gunListesiniGuncelle(personel.personelCalismaGun!!)
            }



            val persDakika=personel.personelCalismaDakika.toString()
            binding.saatText.setText(persDakika)



        }

        binding.RadioGrup.setOnCheckedChangeListener { group, checkedId ->

            if (R.id.gizliBtn == checkedId) personel.personelRandDur = false
            else personel.personelRandDur = true
        }


        binding.kaydetBtn.setOnClickListener {

          //personel objesini kullanacağız, yeni bir personel objesi (guncelPersonel) oluşturmayacagız.

           /* val guncelPersonel = Personel(personel.firmaId,personel.personelId,personel.personelAdi,
                personel.personelTel,personel.personelMail,personel.personelUnvan,personel.personelRandDur,personel.personelCalismaGun)
*/




            val persDakikaAralik= binding.saatText.text.toString().toInt()
            personel.personelCalismaDakika = persDakikaAralik


            if (persDakikaAralik==0) {
                AppUtil.longToast(requireContext(), "Randevu saat aralığı seçiniz..!!")
                return@setOnClickListener
            }
                println(persDakikaAralik)


            AppUtil.personelKaydet(personel){
                println(personel)
                if(it){
                    AppUtil.longToast(requireContext(),"Personel randevu durumu güncellendi..'")
                    findNavController().popBackStack()
                   } else{
            Toast.makeText(requireContext(), "HATA", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.gunlerRecycler.layoutManager=LinearLayoutManager(requireContext())
        binding.gunlerRecycler.adapter=adapter

    }


}