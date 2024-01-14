package com.eneserkocak.randevu.view_ayarlar.gun_saat_ayar

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.adapter.GunSaatPersListAdapter

import com.eneserkocak.randevu.databinding.FragmentGunSaatPersListeBinding
import com.eneserkocak.randevu.model.PERSONELLER
import com.eneserkocak.randevu.model.Personel
import com.eneserkocak.randevu.view.BaseFragment
import com.google.firebase.firestore.FirebaseFirestore

class GunSaatPersListeFragment : BaseFragment<FragmentGunSaatPersListeBinding>(R.layout.fragment_gun_saat_pers_liste) {

    val adapter=GunSaatPersListAdapter(){
        viewModel.secilenPersonel.value=it

    }
    var personelList= listOf<Personel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.gunSaatPersListRecycler.layoutManager=LinearLayoutManager(requireContext())
        binding.gunSaatPersListRecycler.adapter=adapter

        //adapter.personelListesiniGuncelle(personelList)

        getData(){
            personelList = it
            adapter.personelListesiniGuncelle(it)
        }

    }
    fun getData(personeller: (List<Personel>)->Unit){

        FirebaseFirestore.getInstance().collection(PERSONELLER).get().addOnSuccessListener {
            it?.let {
                val personelListesi =  it.toObjects(Personel::class.java)
                personeller.invoke(personelListesi)
            }
            //println()
        }
            .addOnFailureListener {
                println(it)
            }


    }


}