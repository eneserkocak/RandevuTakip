package com.eneserkocak.randevu.view_ayarlar.personel_ayar

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.databinding.FragmentYeniPersonelEkleBinding
import com.eneserkocak.randevu.model.*
import com.eneserkocak.randevu.view.BaseFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class YeniPersonelEkleFragment : BaseFragment<FragmentYeniPersonelEkleBinding>(R.layout.fragment_yeni_personel_ekle) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    binding.personelKydt.setOnClickListener {

        val isim = binding.persIsmi.text.toString()
        val tel = binding.persTel.text.toString()
        val mail = binding.persMail.text.toString()

        if (isim.isEmpty() || tel.isEmpty()){
            Toast.makeText(requireContext()," İsim ve telefon numarası girilmeden personel eklenemez.!",Toast.LENGTH_LONG).show()
            return@setOnClickListener
        }

        FirebaseFirestore.getInstance().collection(PERSONELLER)
            .whereEqualTo(FIRMA_ID,1)
            .orderBy(PERSONEL_ID,Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener {
                it?.let {
                    var yeniPersonelId = 0

                    if (it.isEmpty) yeniPersonelId= 1
                    else {
                        for (document in it) {
                            val sonPersonel = document.toObject(Personel::class.java)
                            yeniPersonelId = sonPersonel.personelId+1

                        }
                    }
                    val personel = Personel(1, yeniPersonelId, isim, tel, mail,)
                    viewModel.secilenPersonel.value = personel
                    println(personel)
                    personelEkle(personel)

                }
            }.addOnFailureListener {
                it.printStackTrace()
            }


       // PersonelDatabase.getInstance(requireContext())?.personelDao()?.insertAll(eklenenPersonel)



       }

    }

    fun personelEkle(personel:Personel) {
            AppUtil.personelKaydet(personel) {
                if(it) {
                    Toast.makeText(requireContext(), "Yeni Personel Eklendi", Toast.LENGTH_LONG).show()

                    findNavController().popBackStack()
                    navigate(R.id.personelListeFragment)
                } else
                    Toast.makeText(requireContext(), "HATA", Toast.LENGTH_LONG).show()
            }
    }

}