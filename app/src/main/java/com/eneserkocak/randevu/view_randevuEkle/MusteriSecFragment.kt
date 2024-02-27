package com.eneserkocak.randevu.view_randevuEkle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.Util.UserUtil
import com.eneserkocak.randevu.adapter.MusteriSecRecyclerAdapter
import com.eneserkocak.randevu.databinding.DialogMusteriSecBinding
import com.eneserkocak.randevu.model.FIRMA_KODU
import com.eneserkocak.randevu.model.MUSTERILER
import com.eneserkocak.randevu.model.Musteri
import com.eneserkocak.randevu.viewModel.AppViewModel
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

const val MUSTERI_SEC = "MusteriSec"

class MusteriSecFragment : DialogFragment() {
    lateinit var binding: DialogMusteriSecBinding
    val viewModel: AppViewModel by activityViewModels()

    var musteriListesi= listOf<Musteri>()
    val adapter=MusteriSecRecyclerAdapter(){
        viewModel.secilenMusteri.value=it
        findNavController().navigate(R.id.randevuAlFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogMusteriSecBinding.inflate(inflater, container, false)
        return binding.root
    }


    private lateinit var searchView: SearchView
    var searchList = mutableListOf<Musteri>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        searchView = binding.search

        searchView.setOnClickListener {
            searchView.isIconified = false
        }

        binding.musteriSecRecycler.layoutManager=LinearLayoutManager(requireContext())
        binding.musteriSecRecycler.adapter=adapter

        getData(){
            musteriListesi = it
            adapter.musteriListesiniGuncelle(it)
        }


        binding.Kapat.setOnClickListener {

            dismiss()
        }
        binding.yeniMusteriEkle.setOnClickListener {
            dismiss()
            val action = MusteriSecFragmentDirections.actionMusteriSecFragmentToYeniMusteriEkleFragment(
                MUSTERI_SEC)
            findNavController().navigate(action)
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

    override fun getTheme(): Int {
        return R.style.musteriSecFragmentFullScreenDialogStyle
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