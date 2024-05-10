package com.eneserkocak.randevu.view_ayarlar.borclular

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.databinding.DialogAlacaklarBilgiNotuBinding
import com.eneserkocak.randevu.viewModel.AppViewModel

class BorclularBilgiNotuDialogFragment : DialogFragment() {


    lateinit var binding: DialogAlacaklarBilgiNotuBinding
    val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogAlacaklarBilgiNotuBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    binding.vazgecBtn.setOnClickListener {
        findNavController().popBackStack()
    }
    binding.musterilereGitBtn.setOnClickListener {
        findNavController().navigate(R.id.musteriListeFragment)
    }


    }



    override fun getTheme(): Int {
        return R.style.randevuIptalFragmentFullScreenDialogStyle

    }

}