package com.eneserkocak.randevu.view_ayarlar.hizmet_ayar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.databinding.DialogHizmetDuzenleBinding
import com.eneserkocak.randevu.model.Hizmet
import com.eneserkocak.randevu.viewModel.AppViewModel

class HizmetDuzenleFragment : DialogFragment() {
    lateinit var binding: DialogHizmetDuzenleBinding
    val viewModel: AppViewModel by activityViewModels()

    lateinit var hizmet:Hizmet


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogHizmetDuzenleBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

           binding.viewModel=viewModel

        viewModel.secilenHizmet.observe (viewLifecycleOwner){ secilenHizmet->

            secilenHizmet?.let {
                hizmet=it

                binding.hizmetAdi.setText(it.hizmetAdi).toString()
                binding.aciklama.setText(it.aciklama).toString()
                binding.fiyat.setText(it.fiyat.toString())

            }

            binding.duzenleBtn.setOnClickListener {

               val hizmetAd= binding.hizmetAdi.text.toString()
                val hizAciklama= binding.aciklama.text.toString()
                val hizFiyat=binding.fiyat.text.toString().toInt()

                val hizmet = Hizmet(hizmet.firmaId,hizmet.hizmetId,hizmetAd,hizAciklama,hizFiyat)

                AppUtil.hizmetKaydet(hizmet){
                    if (it){
                        AppUtil.longToast(context,"Hizmet düzenlendi.")

                        findNavController().popBackStack()
                        findNavController().navigate(R.id.hizmetListeFragment)
                    }
                }
         }

    }
    }
    override fun getTheme(): Int {
        return R.style.hizmetDuzenleFragmentFullScreenDialogStyle

       }
}