package com.eneserkocak.randevu.view_musteri

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.LayoutInflater
import android.view.View
import android.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.Util.UserUtil
import com.eneserkocak.randevu.adapter.MusteriRecyclerAdapter
import com.eneserkocak.randevu.databinding.DialogMusteriDuzenleBinding


import com.eneserkocak.randevu.databinding.FragmentMusteriListeBinding
import com.eneserkocak.randevu.model.FIRMA_KODU
import com.eneserkocak.randevu.model.MUSTERILER
import com.eneserkocak.randevu.model.Musteri
import com.eneserkocak.randevu.view.BaseFragment
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

const val YENI_MUSTERI_EKLE = "YeniMusteriEkle"

class MusteriFragment() : BaseFragment<FragmentMusteriListeBinding>(R.layout.fragment_musteri_liste) {

    var musteriListesi= listOf<Musteri>()
    lateinit var adapter: MusteriRecyclerAdapter
   // private lateinit var db:FirebaseFirestore

    private lateinit var searchView: SearchView
    var searchList = mutableListOf<Musteri>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = MusteriRecyclerAdapter(){
            viewModel.secilenMusteri.value = it
            findNavController().navigate(R.id.musteriDuzenleFragment)
        }
    binding.musteriRecycler.layoutManager= LinearLayoutManager(requireContext())
    binding.musteriRecycler.adapter=adapter

        binding.kisiEkle.setOnClickListener {

       // findNavController().popBackStack()
        val action=MusteriFragmentDirections.actionMusteriFragmentToYeniMusteriEkleFragment(YENI_MUSTERI_EKLE)
        findNavController().navigate(action)
        //navigate(R.id.yeniMusteriEkleFragment)
        }



        searchView = binding.search

        searchView.setOnClickListener {
            searchView.isIconified = false
        }


      getData(){
          musteriListesi = it

         val list= musteriListesi.sortedBy{
              it.musteriAdi
          }

          adapter.musteriListesiniGuncelle(list)
      }

        search()


    }


    fun getData(musteriler : (List<Musteri>)->Unit){

   FirebaseFirestore.getInstance().collection(MUSTERILER)
       .whereEqualTo(FIRMA_KODU,UserUtil.firmaKodu)
       .get().addOnSuccessListener {
           it?.let {
               val musteriListesi =  it.toObjects(Musteri::class.java)
               musteriler.invoke(musteriListesi)
           }
                   //println()
                }
                .addOnFailureListener {
                    println(it)
                }


    }

     private fun mustDuzDialogGoster(it: Musteri) {

        val binding = DialogMusteriDuzenleBinding.inflate(LayoutInflater.from(requireContext()))
        binding.musteri = it
        val dialog = AlertDialog.Builder(requireContext()).setView(binding.root).show()


    }


    private fun search() {
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {

                searchList.clear()

                val searchText= newText!!.toLowerCase(Locale.getDefault())

                if (searchText.isNotEmpty()){
                   musteriListesi.forEach{
                        if (it.musteriAdi.uppercase(Locale.getDefault()).contains(searchText.uppercase(
                                Locale.US))){
                            searchList.add(it)
                        }
                    }
                    adapter.musteriListesiniGuncelle(searchList.toList())

                }else{
                    adapter.musteriListesiniGuncelle(musteriListesi)
                }
                return true
            }


        })



    }



}