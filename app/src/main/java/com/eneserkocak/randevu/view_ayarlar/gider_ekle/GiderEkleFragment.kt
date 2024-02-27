package com.eneserkocak.randevu.view_ayarlar.gider_ekle

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil

import com.eneserkocak.randevu.Util.UserUtil
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

    //MİKROFONDAN 12500 GİBİBİ RAKAM SÖYLEYİNCE ORTAYA NOKTA KOYUYOR (12.500) VE ÇÖKÜYOR..ENGELLEMEK İÇİN AŞAĞIDAKİ KOD::
        val gider=tutar.replace(Regex("[^a-zA-Z0-9\\s]"), "")

            if (tutar.isEmpty() || giderAdi.isEmpty()){
                AppUtil.longToast(requireContext(),"Gider adı ve tutarı girilmeden gider eklenemez!..")
                return@setOnClickListener
            }

            val giderTutar=gider.toInt()

            FirebaseFirestore.getInstance().collection(GIDERLER)
                .whereEqualTo(FIRMA_KODU,UserUtil.firmaKodu)
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
                        val gider= Gider(UserUtil.firmaKodu,yeniGiderId,giderAdi,giderTutar,tarih)

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

        binding.layoutGiderAdi.setEndIconOnClickListener {
            askSpeechInput(124)
        }
        binding.layoutGiderFiyat.setEndIconOnClickListener {
            askSpeechInput(125)
        }

  }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ( resultCode== Activity.RESULT_OK){
            val result=data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            when (requestCode){
                124-> binding.giderAdi.setText(result?.get(0).toString())
                125-> binding.giderTutar.setText(result?.get(0).toString())


            }
        }
    }
    private fun askSpeechInput(requestCode:Int) {
        if (!SpeechRecognizer.isRecognitionAvailable(requireContext())){
            AppUtil.longToast(requireContext(),"Konuşma tanıma kullanılamıyor..!")
        }else{
            val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Kaydetmek istediğinizi söyleyin..!")
            startActivityForResult(i,requestCode)
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


