package com.eneserkocak.randevu.view_ayarlar.gun_saat_ayar

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.Util.UserUtil
import com.eneserkocak.randevu.adapter.GunSaatPersListAdapter

import com.eneserkocak.randevu.databinding.FragmentGunSaatPersListeBinding
import com.eneserkocak.randevu.model.FIRMA_KODU
import com.eneserkocak.randevu.model.PERSONELLER
import com.eneserkocak.randevu.model.Personel
import com.eneserkocak.randevu.view.BaseFragment
import com.google.firebase.firestore.FirebaseFirestore

class GunSaatPersListeFragment : BaseFragment<FragmentGunSaatPersListeBinding>(R.layout.fragment_gun_saat_pers_liste) {

    lateinit var adapter: GunSaatPersListAdapter
    var personelList= listOf<Personel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter=GunSaatPersListAdapter(){
            viewModel.secilenPersonel.value=it

        }

        binding.gunSaatPersListRecycler.layoutManager=LinearLayoutManager(requireContext())
        binding.gunSaatPersListRecycler.adapter=adapter


        getData(){
            personelList = it
            val list=personelList.sortedBy {
                it.personelAdi
            }
            adapter.personelListesiniGuncelle(list)
        }

    }
    fun getData(personeller: (List<Personel>)->Unit){

        FirebaseFirestore.getInstance().collection(PERSONELLER)
            .whereEqualTo(FIRMA_KODU,UserUtil.firmaKodu)
            .get().addOnSuccessListener {
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