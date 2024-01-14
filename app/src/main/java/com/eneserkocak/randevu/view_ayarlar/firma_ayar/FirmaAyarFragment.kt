package com.eneserkocak.randevu.view_ayarlar.firma_ayar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.databinding.DialogFirmaAyarBinding
import com.eneserkocak.randevu.viewModel.AppViewModel

class FirmaAyarFragment : DialogFragment() {


    lateinit var binding: DialogFirmaAyarBinding
    val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFirmaAyarBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.firmaBilgiBtn.setOnClickListener {
            findNavController().navigate(R.id.firmaBilgileriFragment)
        }

        binding.firmaKonumBtn.setOnClickListener {
           // findNavController().navigate(R.id.firmaMapsFragment)
            val action = FirmaAyarFragmentDirections.actionFirmaAyarFragmentToFirmaMapsFragment(FIRMA_AYAR_FRAGMENT)
            findNavController().navigate(action)

            AppUtil.longToast(requireContext(),"Firmanıza bir den fazla konum ekleyebilirsiniz..")
        }


    }

    override fun getTheme(): Int {
        return R.style.firmaAyarFragmentFullScreenDialogStyle

    }



}