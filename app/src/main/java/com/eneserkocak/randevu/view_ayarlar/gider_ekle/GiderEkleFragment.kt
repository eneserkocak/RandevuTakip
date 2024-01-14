package com.eneserkocak.randevu.view_ayarlar.gider_ekle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.Util.toTimestamp
import com.eneserkocak.randevu.databinding.DialogGiderEkleBinding
import com.eneserkocak.randevu.model.*
import com.eneserkocak.randevu.viewModel.AppViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*


class GiderEkleFragment : DialogFragment() {
    lateinit var binding: DialogGiderEkleBinding
    val viewModel: AppViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogGiderEkleBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.giderKaydetBtn.setOnClickListener {

        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val tarihText = sdf.format(cal.time)
        val tarih=cal.time.toTimestamp()

        val giderAdi=binding.giderAdi.text.toString()
        val tutar=binding.giderTutar.text.toString()

            if (tutar.isEmpty() || giderAdi.isEmpty()){
                AppUtil.longToast(requireContext(),"Gider adı ve tutarı girilmeden gider eklenemez!..")
                return@setOnClickListener
            }

            val giderTutar=tutar.toInt()

            FirebaseFirestore.getInstance().collection(GIDERLER)
                .whereEqualTo(FIRMA_ID,1)
                .orderBy(GIDER_ID, Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener {
                    it?.let {
                        var yeniGiderId=0

                        if(it.isEmpty) yeniGiderId=1
                        else{
                            for (document in it){
                                val sonGider= document.toObject(Gider::class.java)
                                yeniGiderId= sonGider.giderId+1
                            }
                        }
                        val gider= Gider(1,yeniGiderId,giderAdi,giderTutar,tarih)

                        viewModel.secilenGider.value=gider
                        giderEkle(gider)
                    }


                }.addOnFailureListener {
                    it.printStackTrace()
                }
       }


        binding.vazgecBtn.setOnClickListener {
            findNavController().popBackStack()
        }
  }

    fun giderEkle(gider:Gider){
        AppUtil.giderKaydet(gider){
            if (it){
                Toast.makeText(requireContext(), "Yeni Gider Eklendi", Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
                findNavController().navigate(R.id.ayarlarFragment)
            }else{
                Toast.makeText(requireContext(), "HATA", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun getTheme(): Int {
        return R.style.giderEkleFragmentFullScreenDialogStyle

    }
 }


