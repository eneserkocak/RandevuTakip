package com.eneserkocak.randevu.view_ayarlar.personel_ayar

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.Util.UserUtil
import com.eneserkocak.randevu.adapter.PersonelRecyclerAdapter

import com.eneserkocak.randevu.databinding.FragmentPersonelListeBinding
import com.eneserkocak.randevu.model.FIRMA_KODU

import com.eneserkocak.randevu.model.PERSONELLER
import com.eneserkocak.randevu.model.Personel
import com.eneserkocak.randevu.model.PersonelRandevu
import com.eneserkocak.randevu.view.BaseFragment

import com.google.firebase.firestore.FirebaseFirestore


class PersonelListeFragment : BaseFragment<FragmentPersonelListeBinding>(R.layout.fragment_personel_liste) {

    var personelListesi= listOf<Personel>()

    val adapter = PersonelRecyclerAdapter(){

        viewModel.secilenPersonel.value=it

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.personelEkleRecycler.layoutManager= LinearLayoutManager(requireContext())
        binding.personelEkleRecycler.adapter= adapter

       binding.personelEkleBtn.setOnClickListener {
           findNavController().popBackStack()
           navigate(R.id.yeniPersonelEkleFragment)

   }

        getData(){
            personelListesi = it
            adapter.personelListesiniGuncelle(it)
        }


    }

    fun getData(personeller: (List<Personel>)->Unit){

        FirebaseFirestore.getInstance().collection(PERSONELLER)
            .whereEqualTo(FIRMA_KODU,UserUtil.firmaKodu)
            .get().addOnSuccessListener {
            it?.let {
                if (!it.isEmpty) {
                    val personelListesi =  it.toObjects(Personel::class.java)
                    personeller.invoke(personelListesi)
                }

            }
            //println()
        }
            .addOnFailureListener {
                println(it)
            }


    }
}

