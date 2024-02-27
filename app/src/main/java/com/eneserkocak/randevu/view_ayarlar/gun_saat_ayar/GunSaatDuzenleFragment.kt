package com.eneserkocak.randevu.view_ayarlar.gun_saat_ayar

import android.annotation.SuppressLint
import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
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
import com.eneserkocak.randevu.model.PERS_DAKIKA

import com.eneserkocak.randevu.model.Personel

import com.eneserkocak.randevu.view.BaseFragment
import java.util.*


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



                val persDakika = personel.personelCalismaDakika.toString()
                binding.saatText.setText(persDakika)


        }

        binding.RadioGrup.setOnCheckedChangeListener { group, checkedId ->


            if (R.id.gizliBtn == checkedId) personel.personelRandDur = false
            else personel.personelRandDur = true
        }


        binding.kaydetBtn.setOnClickListener {

          //personel objesini kullan, yeni bir personel objesi (guncelPersonel) oluşturma.

           /* val guncelPersonel = Personel(personel.firmaId,personel.personelId,personel.personelAdi,
                personel.personelTel,personel.personelMail,personel.personelUnvan,personel.personelRandDur,personel.personelCalismaGun)
*/


            var persDakikaAralik= binding.saatText.text.toString().toInt()
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


        binding.layoutDakika.setEndIconOnClickListener {
            askSpeechInput(117)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ( resultCode== Activity.RESULT_OK){
            val result=data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            when (requestCode){
                117-> binding.saatText.setText(result?.get(0).toString())

            }
        }
    }
    private fun askSpeechInput(requestCode:Int) {
        if (!SpeechRecognizer.isRecognitionAvailable(requireContext())){
            AppUtil.longToast(requireContext(),"Konuşma tanıma kullanılamıyor..!")
        }else{
            val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Kaydetmek istediğinizi söyleyin..!")
            startActivityForResult(i,requestCode)
        }
    }


}