package com.eneserkocak.randevu.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.eneserkocak.randevu.viewModel.AppViewModel


abstract class BaseFragment<DB : ViewDataBinding>(@LayoutRes private val layoutResId : Int) : Fragment(){


    lateinit var binding : DB
    val viewModel : AppViewModel by activityViewModels()




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<DB>(inflater, layoutResId, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root



    }
    //Fragment geçişleri için val action yapıp tek tek kod yazmaya gerek kalmayacak
    fun navigate(yeniFragmentId: Int){
        findNavController().navigate(yeniFragmentId)

    }






}