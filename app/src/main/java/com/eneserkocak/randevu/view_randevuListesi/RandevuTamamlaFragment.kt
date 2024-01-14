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
import com.eneserkocak.randevu.databinding.DialogRandevuTamamlaBinding
import com.eneserkocak.randevu.model.*
import com.eneserkocak.randevu.viewModel.AppViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat

/*fun Timestamp.toSaat(): Timestamp {
    val sdf = SimpleDateFormat("HH:mm")
    val saat = sdf.format(this)
    return sdf.parse(saat)
}*/

// BURASI İPTALLLLL......DİALOG A ÇEVİRDİM...


class RandevuTamamlaFragment : DialogFragment() {

     var randevuGeliri:Int=0
    lateinit var randevu: Randevu
    lateinit var randevuDurumu:String

    lateinit var binding: DialogRandevuTamamlaBinding
    val viewModel: AppViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogRandevuTamamlaBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.secilenRandevu.observe(viewLifecycleOwner){ secilenRandevu->
            secilenRandevu?.let {
                randevu=it


           /* binding.dialogTamamlaBtn.setOnClickListener {

//eğer randevu geliri girmeden tamamlaya basarsak randGelir text string geliyo ınt e ceviremediği için çöküyor

            var gelir =binding.randGelir.text

            val personelAdi= randevu.personel
            val randevuTime= randevu.randevuTime


            randevuGeliri= if (gelir.isEmpty()) randevu.hizmet.fiyat
            else binding.randGelir.text.toString().toInt()

            randevuDurumu="Randevu Tamamlandı"



            *//*val sharedPreferences = AppUtil.getSharedPreferences(requireContext())
            sharedPreferences.edit().putString(RANDEVU_GELIR,randGeliri).apply()*//*

            println("YAZDIR: ${personelAdi}")
            println("YAZDIR: ${randevuTime}")



            val randevuMap = mapOf<String,Any>(
                RANDEVU_GELIRI to randevuGeliri,
                RANDEVU_TAMAMLA to randevuDurumu
            )


            FirebaseFirestore.getInstance().collection(RANDEVULAR).document(AppUtil.randevuDocumentPath(randevu))
                .update(randevuMap)

            dismiss()

            AppUtil.longToast(requireContext(),"Randevu Tamamlandı..!")


         }*/
       }
    }

        binding.vazgecBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun getTheme(): Int {
        return R.style.randevuTamamlaFragmentFullScreenDialogStyle

    }


}