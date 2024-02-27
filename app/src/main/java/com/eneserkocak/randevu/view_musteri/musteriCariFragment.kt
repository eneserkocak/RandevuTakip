package com.eneserkocak.randevu.view_musteri

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.isEmpty
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.UserUtil
import com.eneserkocak.randevu.adapter.MusteriVeresiyeAdapter
import com.eneserkocak.randevu.adapter.PersonelRandevuAdapter
import com.eneserkocak.randevu.databinding.FragmentMusteriCariBinding
import com.eneserkocak.randevu.model.*
import com.eneserkocak.randevu.view.BaseFragment
import com.google.firebase.firestore.FirebaseFirestore


class musteriCariFragment : BaseFragment<FragmentMusteriCariBinding>(R.layout.fragment_musteri_cari) {

    lateinit var secilenMusteri: Musteri
    var filtreMustBorcListesi= listOf<Randevu>()

    val adapter= MusteriVeresiyeAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.musteriVeresiyeRecycler.layoutManager= LinearLayoutManager(requireContext())
        binding.musteriVeresiyeRecycler.adapter= adapter



        viewModel.veresiyeRandVerileriGetir()
        viewModel.veresiyeTamamlananRandevuListesi.observe(viewLifecycleOwner){

            it?.let {

                viewModel.secilenMusteri.observe(viewLifecycleOwner) { secilenMusteri ->
                    secilenMusteri?.let { musteri ->


                        binding.musteriAdiTxt.text=musteri.musteriAdi
                        filtreMustBorcListesi = it.filter {
                            it.musteri.musteriAdi == secilenMusteri.musteriAdi
                        }

                        println("CARİİİ: ${filtreMustBorcListesi.size}")


                        if (filtreMustBorcListesi.isEmpty()) {
                            binding.toplamBorcCount.setText("0")
                            binding.musteriVeresiyeRecycler.visibility = View.GONE
                            binding.bilgiText.setText("Müşteriye ait borç bilgisi bulunmamaktadır..!")
                            binding.bilgiText.setTextColor(Color.parseColor("#C53D3D"))
                            binding.bilgiText.visibility = View.VISIBLE

                        } else {

                            var borc = 0
                            filtreMustBorcListesi.forEach {
                                borc = borc + it.veresiyeTutari
                                binding.toplamBorcCount.text = borc.toString()
                            }
                            binding.musteriVeresiyeRecycler.visibility = View.VISIBLE
                            binding.bilgiText.visibility = View.GONE

                            adapter.musteriVeresiyeListesiniGuncelle(filtreMustBorcListesi)

                    //MUSTERİ SINIFINA BORÇ TUTARINI EKLE -> FİREBASE  -> borç sms için burda kaydettiğimiz toplam borcu kullan
                            val mustDocument = UserUtil.firmaKodu.toString()+"-"+musteri.musteriId.toString()
                            val musteriBorc= borc

                            val musteriMap= mapOf<String,Any>(
                                MUSTERI_BORC to musteriBorc
                            )

                            FirebaseFirestore.getInstance().collection(MUSTERILER).document(mustDocument)
                                .update(musteriMap)
                        }



                        //MUSTERİ VERESİYE RECYCLER LİST BOŞ İSE FİREBASE DE-> MÜSTERİ MODEL-> musteriVeresiye -> FALSE YAP


                        if (binding.musteriVeresiyeRecycler.isEmpty() && binding.toplamBorcCount.text =="0") {
                            //Müşteri Sınıfında Borcu Var -> False YAP
                            val mustDocument = UserUtil.firmaKodu.toString()+"-"+musteri.musteriId.toString()
                            val musteriVeresiye= false

                            val musteriMap= mapOf<String,Any>(
                                  MUSTERI_VERESIYE to musteriVeresiye


                            )

                            FirebaseFirestore.getInstance().collection(MUSTERILER).document(mustDocument)
                                .update(musteriMap)

                        }else{
                            binding.musteriVeresiyeRecycler.visibility=View.VISIBLE

                            val mustDocument = UserUtil.firmaKodu.toString()+"-"+musteri.musteriId.toString()
                            val musteriVeresiye= true

                            val musteriMap= mapOf<String,Any>(
                                MUSTERI_VERESIYE to musteriVeresiye
                            )

                            FirebaseFirestore.getInstance().collection(MUSTERILER).document(mustDocument)
                                .update(musteriMap)

                        }




                    }

                }

            }}

        binding.msjButton.setOnClickListener {
                findNavController().navigate(R.id.smsBorcFragment)

        }
    }
}