package com.eneserkocak.randevu.view_ayarlar.gun_ozeti

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.databinding.DialogGunOzetiSecBinding
import com.eneserkocak.randevu.viewModel.AppViewModel

class GunOzetiSecFragment : DialogFragment() {


    lateinit var binding: DialogGunOzetiSecBinding
    val viewModel: AppViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogGunOzetiSecBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

   binding.gelirOzetiBtn.setOnClickListener {
       findNavController().navigate(R.id.gunOzetiFragment)
   }

        binding.giderOzetiBtn.setOnClickListener {
            findNavController().navigate(R.id.gunOzetiGiderFragment)
        }


    }

    override fun getTheme(): Int {
        return R.style.gunOzetiSecFragmentFullScreenDialogStyle

    }


}