package com.eneserkocak.randevu.view_ayarlar.ayarlar

import android.os.Bundle
import android.view.View
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.databinding.FragmentAyarlarBinding
import com.eneserkocak.randevu.view.BaseFragment
import com.google.protobuf.Empty


class AyarlarFragment : BaseFragment<FragmentAyarlarBinding>(R.layout.fragment_ayarlar) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.firmaAyarlari.setOnClickListener {
            navigate(R.id.firmaAyarFragment)
        }

        binding.personelBilgileri.setOnClickListener {
            navigate(R.id.personelListeFragment)
        }

        binding.hizmetAyarlari.setOnClickListener {
            navigate(R.id.hizmetListeFragment)
        }

        binding.gunSaatAyar.setOnClickListener {
            navigate(R.id.gunSaatPersListeFragment)
        }

        binding.gunOzeti.setOnClickListener {
            navigate(R.id.gunOzetiSecFragment)
        }
        binding.raporlar.setOnClickListener {
            viewModel.raporRandevuListesi.value=emptyList()
            navigate(R.id.raporlarFragment)
        }

        binding.giderler.setOnClickListener {
            navigate(R.id.giderEkleFragment)
        }



    }

}