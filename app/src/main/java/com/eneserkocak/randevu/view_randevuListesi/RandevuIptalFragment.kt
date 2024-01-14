package com.eneserkocak.randevu.view_randevuListesi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.databinding.DialogRandevuIptalBinding
import com.eneserkocak.randevu.databinding.DialogRandevuTamamlaBinding
import com.eneserkocak.randevu.model.*
import com.eneserkocak.randevu.viewModel.AppViewModel
import com.google.firebase.firestore.FirebaseFirestore


class RandevuIptalFragment : DialogFragment() {

    lateinit var randevu: Randevu
    lateinit var randevuDurumu:String

    lateinit var binding: DialogRandevuIptalBinding
    val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogRandevuIptalBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.secilenRandevu.observe(viewLifecycleOwner){ secilenRandevu->
            secilenRandevu?.let {
                randevu=it
            }

        }


   /*     binding.iptalBtn.setOnClickListener {


            val clickListener: (ClickedButton) -> Unit = {
               binding.clickedButton=it
            }
            clickListener.invoke(ClickedButton.İptal)



            randevuDurumu="İptal Edildi"

            val randevuMap = mapOf<String,Any>(

                RANDEVU_TAMAMLA to randevuDurumu
            )

            FirebaseFirestore.getInstance().collection(RANDEVULAR).document(AppUtil.randevuDocumentPath(randevu))
                .update(randevuMap)

            dismiss()

            AppUtil.longToast(requireContext(),"Randevu İptal Edildi..!")

        }*/



    }
    override fun getTheme(): Int {
        return R.style.randevuIptalFragmentFullScreenDialogStyle

    }


}