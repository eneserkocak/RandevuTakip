package com.eneserkocak.randevu.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.Util.PictureUtil
import com.eneserkocak.randevu.databinding.FragmentGirisBinding
import com.eneserkocak.randevu.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class GirisFragment : BaseFragment<FragmentGirisBinding>(R.layout.fragment_giris) {

    private lateinit var auth : FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth= Firebase.auth



       // replaceFragment(GirisFragment())

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
            //navigate(R.id.randevuListesiFragment)
           findNavController().navigate(R.id.randevuListesiFragment)
        }

                val sharedPref = AppUtil.getSharedPreferences(requireContext())
                val firmaAdiAl = sharedPref.getString(FIRMA_ADI, "boş")
                val telNoAl = sharedPref.getString(FIRMA_TEL, "boş")
                val adresAl = sharedPref.getString(FIRMA_ADRES, "boş")


                binding.firmaAdiText.setText(firmaAdiAl.toString())
                binding.firmaTelText.setText(telNoAl.toString())
                binding.firmaAdresText.setText(adresAl.toString())

                PictureUtil.firmaGorselIndir(requireContext(),binding.firmaLogoImage)


             //   binding.firmaLogoImage.setImageBitmap()

   /* binding.bottomNavigationView.setOnItemSelectedListener {

        when(it.itemId){

            R.id.anasayfa -> replaceFragment(GirisFragment())
            R.id.randevuEkle-> replaceFragment(RandevuAlFragment())
            R.id.randevuList-> replaceFragment(RandevuListesiFragment())
            R.id.ayarlar-> replaceFragment(AyarlarFragment())
            R.id.musteriler-> replaceFragment(MusteriFragment())

            else->{

            }
        }
        true
    }*/

    }

    private fun replaceFragment(fragment:Fragment){

        val fragmentManager=requireActivity().supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.linearLayout,fragment)
        fragmentTransaction.commit()
    }




    override fun onCreate(savedInstanceState: Bundle?) {
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
    }
}


