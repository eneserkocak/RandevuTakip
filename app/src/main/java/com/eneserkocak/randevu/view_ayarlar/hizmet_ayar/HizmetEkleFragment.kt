package com.eneserkocak.randevu.view_ayarlar.hizmet_ayar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.databinding.DialogHizmetEkleBinding
import com.eneserkocak.randevu.model.*
import com.eneserkocak.randevu.viewModel.AppViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HizmetEkleFragment: DialogFragment() {
    lateinit var binding: DialogHizmetEkleBinding
    val viewModel: AppViewModel by activityViewModels()

   // var fiyat:Int=0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogHizmetEkleBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.hzmtKaydetBtn.setOnClickListener {

            val hizmetAdi = binding.hizmetAdi.text.toString()
            val aciklama = binding.aciklama.text.toString()

            var hizFiyati=0
            val fiyat=binding.fiyat.text

           val hizmetFiyati= if (fiyat.isEmpty()) hizFiyati
            else binding.fiyat.text.toString().toInt()

            //FİYAT I BOŞ BIRAKINCA STRING GELDİĞİ İÇİN INT E DÖNDÜREMİYO VE ÇÖKÜYOR

            if (hizmetAdi.isEmpty()){
                AppUtil.longToast(requireContext(),"Hizmet adı girilmeden kaydedilemez!..")
                return@setOnClickListener
            }


            FirebaseFirestore.getInstance().collection(HIZMETLER)
                .whereEqualTo(FIRMA_ID,1)
                .orderBy(HIZMET_ID,Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener {
                    it?.let {
                        var yeniHizmetId=0

                        if(it.isEmpty) yeniHizmetId=1
                        else{
                            for (document in it){
                                val sonHizmet= document.toObject(Hizmet::class.java)
                                yeniHizmetId= sonHizmet.hizmetId+1
                            }
                        }
                        val hizmet=Hizmet(1,yeniHizmetId,hizmetAdi,aciklama,hizmetFiyati)

                        viewModel.secilenHizmet.value=hizmet
                        hizmetEkle(hizmet)
                    }


                }.addOnFailureListener {
                    it.printStackTrace()
            }

        }

    }
    fun hizmetEkle(hizmet:Hizmet) {
        AppUtil.hizmetKaydet(hizmet) {
            if(it) {
                Toast.makeText(requireContext(), "Yeni hizmet eklendi", Toast.LENGTH_LONG).show()

                findNavController().popBackStack()
                findNavController().navigate(R.id.hizmetListeFragment)
            } else
                Toast.makeText(requireContext(), "HATA", Toast.LENGTH_LONG).show()
        }
    }

    //FİRMA ID VE HİZMET ID VERMEDEN ÖNCEKİ (KLASİK) KAYDETME YÖNTEMİ:
    /*fun hizmetEkle(hizmet: Hizmet) {


        hizmet.let {
            val hizmetHashMap = hashMapOf<String, Any>()

            hizmetHashMap.put("hizmetAdi", it.hizmetAdi)
            hizmetHashMap.put("aciklama", it.aciklama)
            hizmetHashMap.put("fiyat", it.fiyat)


            println(hizmetHashMap)
            FirebaseFirestore.getInstance().collection(HIZMETLER).document(it.hizmetAdi).set(hizmetHashMap)
                .addOnSuccessListener {

                    println()
                }
                .addOnFailureListener {
                    println(it)
                }
        }

    }*/


    override fun getTheme(): Int {
        return R.style.hizmetEkleFragmentFullScreenDialogStyle

    }
}