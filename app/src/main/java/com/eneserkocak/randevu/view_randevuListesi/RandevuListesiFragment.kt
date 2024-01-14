package com.eneserkocak.randevu.view_randevuListesi

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.toTarih
import com.eneserkocak.randevu.adapter.RandevuListeAdapter
import com.eneserkocak.randevu.databinding.DialogRandevuNotlarBinding
import com.eneserkocak.randevu.databinding.FragmentRandevuListesiBinding
import com.eneserkocak.randevu.model.*
import com.eneserkocak.randevu.view.BaseFragment
import com.eneserkocak.randevu.viewModel.AppViewModel
import com.eneserkocak.randevu.view_musteri.filtreRandevuListesi
import com.eneserkocak.randevu.view_randevuEkle.RANDEVULAR
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

//Extension function: Aşağıda filtrede iki tarafıda tarih değerlerini alıp, eşleştirmek için yaptık:UTİL DE

/*fun Date.toTarih(): Date {
    val sdf = SimpleDateFormat("dd.MM.yyyy")
    val tarih = sdf.format(this)
    return sdf.parse(tarih)
}*/


class RandevuListesiFragment : BaseFragment<FragmentRandevuListesiBinding>(R.layout.fragment_randevu_listesi) {


    var randevuListesi = listOf<Randevu>()
    var filtreRandevuListesi= listOf<Randevu>()



    val adapter= RandevuListeAdapter(){
        viewModel.secilenRandevu.value=it


    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    binding.randevuListRecycler.layoutManager=LinearLayoutManager(requireContext())
    binding.randevuListRecycler.adapter=adapter


            //Seçili güne ait randevu bulunmamaktadır..!



        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val cal = Calendar.getInstance().apply {
                set(year,month,dayOfMonth)
                //.apply yerine
                //cal.set(year,month,dayOfmonth) ta yapabilirdik..


            }


             filtreRandevuListesi = randevuListesi.filter { it.randevuTime.toDate().toTarih() == cal.time.toTarih()}
             adapter.randevuListesiniGuncelle(filtreRandevuListesi)

                println(randevuListesi)

            if (filtreRandevuListesi.size==0){
                binding.bilgiText.setText("Seçili güne ait randevu bulunmamaktadır..!")
                binding.bilgiText.setTextColor(Color.parseColor("#C53D3D"))
                binding.bilgiText.visibility=View.VISIBLE
                binding.randevuListRecycler.visibility=View.GONE
            }else{
                binding.bilgiText.visibility=View.GONE
                binding.randevuListRecycler.visibility=View.VISIBLE
            }

        }

    viewModel.randVerileriGetir()
        viewModel.randevuListesi.observe(viewLifecycleOwner){
            it?.let {
                randevuListesi = it
                //adapter.randevuListesiniGuncelle(it)
            }
         }
     }
  }