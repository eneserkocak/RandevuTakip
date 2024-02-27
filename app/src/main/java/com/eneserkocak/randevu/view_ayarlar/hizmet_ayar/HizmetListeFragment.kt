package com.eneserkocak.randevu.view_ayarlar.hizmet_ayar

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.Util.UserUtil
import com.eneserkocak.randevu.adapter.HizmetListeRecyclerAdapter

import com.eneserkocak.randevu.databinding.FragmentHizmetListeBinding

import com.eneserkocak.randevu.model.*
import com.eneserkocak.randevu.view.BaseFragment
import com.google.firebase.firestore.FirebaseFirestore


class HizmetListeFragment : BaseFragment<FragmentHizmetListeBinding>(R.layout.fragment_hizmet_liste) {

    var hizmetList= listOf<Hizmet>()
    val adapter= HizmetListeRecyclerAdapter(){
        viewModel.secilenHizmet.value=it
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.hizmetEkleBtn.setOnClickListener {

            var hizmetEkleDialogGoster = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_hizmet_ekle,null)
            val alertDialog =AlertDialog.Builder(requireContext())
            alertDialog.setView(hizmetEkleDialogGoster)
            //alertDialog.show()

            findNavController().popBackStack()
            navigate(R.id.hizmetEkleFragment)

        }



        //dao= HizmetDatabase.getInstance(requireContext())!!.hizmetDao()
       // hizmetList= dao.getAll()


        //adapter.hizmetListesiniGuncelle(hizmetList)

    binding.hizmetListeRecycler.layoutManager= LinearLayoutManager(requireContext())
    binding.hizmetListeRecycler.adapter= adapter

        getData(){
            hizmetList = it
            adapter.hizmetListesiniGuncelle(it)
            //adapter.notifyDataSetChanged()
        }

    }

    fun getData(hizmetler : (List<Hizmet>)->Unit){

        FirebaseFirestore.getInstance().collection(HIZMETLER)
            .whereEqualTo(FIRMA_KODU,UserUtil.firmaKodu)
            .get().addOnSuccessListener {
            it?.let {
                val hizmetList =  it.toObjects(Hizmet::class.java)
                hizmetler.invoke(hizmetList)
            }
            //println()
        }
            .addOnFailureListener {
                println(it)
            }


    }







}