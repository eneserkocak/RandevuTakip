package com.eneserkocak.randevu.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.Util.PictureUtil
import com.eneserkocak.randevu.Util.UserUtil
import com.eneserkocak.randevu.databinding.FragmentGirisBinding
import com.eneserkocak.randevu.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source


class GirisFragment : BaseFragment<FragmentGirisBinding>(R.layout.fragment_giris) {

    private lateinit var auth : FirebaseAuth

    var firmaListesi= listOf<Firma>()





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    //ARRAY LİSTEDE ID Sİ OLMAYAN....SİLİNEN KULLANICILARI ATMAK İÇİN...

        auth= FirebaseAuth.getInstance()

        val userId=auth.currentUser?.uid

        FirebaseFirestore.getInstance().collection(FIRMALAR).whereArrayContains("kullanicilar",userId!!)
            .get(Source.SERVER)
            .addOnSuccessListener { document->


                if (document.isEmpty || document ==null){
                    AppUtil.longToast(requireContext(),"Sisteme giriş yetkiniz yoktur..!")
                    auth.signOut()
                      findNavController().navigate(R.id.authenticationFragment)

                }
            }

//PATRON KONTROLÜ
        FirebaseFirestore.getInstance().collection(FIRMALAR).document(UserUtil.firmaKodu.toString()).collection(
            KULLANICILAR).document(userId).get().addOnSuccessListener {
                it?.let {

                    val patronMu = it.get("patron").toString().toBoolean()
                    if (!patronMu){
                        binding.ayarlar.visibility=View.GONE
                    } else{
                        binding.ayarlar.visibility=View.VISIBLE
                    }
                }
        }

        /*val db = Room.databaseBuilder(requireContext(), FirmaDatabase::class.java, "Firma").allowMainThreadQueries().build()
        val firmaDao = db.firmaDao()
        val firmaBilgileri = firmaDao.getAll()*/

        binding.ayarlar.setOnClickListener {
            findNavController().navigate(R.id.ayarlarFragment)
        }

        binding.randevuAl.setOnClickListener {
            viewModel.secilenMusteri.value=null
            navigate(R.id.randevuAlFragment)
        }

        binding.musteriler.setOnClickListener {
            navigate(R.id.musteriListeFragment)
        }



        binding.randevular.setOnClickListener {
           findNavController().navigate(R.id.randevuListesiFragment)
        }

        getData(){
            firmaListesi = it


           val firma= firmaListesi.get(0)


           // println("firma adı: ${firma.firmaAdi}")

            UserUtil.firmaIsim=firma.firmaAdi

            binding.firmaAdiText.setText(firma.firmaAdi)
            binding.firmaTelText.setText(firma.firmaTel)
            binding.firmaAdresText.setText(firma.firmaAdres)
       

        }

        /*     val sharedPref = AppUtil.getSharedPreferences(requireContext())
                val firmaAdiAl = sharedPref.getString(FIRMA_ADI, "boş")
                val telNoAl = sharedPref.getString(FIRMA_TEL, "boş")
                val adresAl = sharedPref.getString(FIRMA_ADRES, "boş")

                binding.firmaAdiText.setText(firmaAdiAl.toString())
                binding.firmaTelText.setText(telNoAl.toString())
                binding.firmaAdresText.setText(adresAl.toString())*/


                PictureUtil.firmaGorselIndir(requireContext(),binding.firmaLogoImage)


    }
    fun getData(firmalar: (List<Firma>)->Unit){

        FirebaseFirestore.getInstance().collection(FIRMALAR)
           .whereEqualTo(FIRMA_KODU,UserUtil.firmaKodu)
            .get().addOnSuccessListener {
                it?.let {
                    if (!it.isEmpty) {
                        val firmaListesi =  it.toObjects(Firma::class.java)
                        firmalar.invoke(firmaListesi)
                    }

                }
                //println()
            }
            .addOnFailureListener {
                println(it)
            }


    }




    /*override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.cikis_menu,menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        auth.signOut()
        findNavController().navigate(R.id.authenticationFragment)
        return super.onOptionsItemSelected(item)
    }*/
}


