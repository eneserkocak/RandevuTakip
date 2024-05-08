package com.eneserkocak.randevu.view_ayarlar.hizmet_ayar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.databinding.DialogHizmetDuzenleBinding
import com.eneserkocak.randevu.model.ACIK
import com.eneserkocak.randevu.model.Hizmet
import com.eneserkocak.randevu.model.KAPALI
import com.eneserkocak.randevu.viewModel.AppViewModel
import java.util.*

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

                val checkedId = if (!hizmet.hizmetGorDur) R.id.kapaliBtn else R.id.gorunBtn
                binding.RadioGrup.check(checkedId)
            }
            binding.RadioGrup.setOnCheckedChangeListener { group, checkedId ->

                if (R.id.kapaliBtn == checkedId) hizmet.hizmetGorDur = false
                else hizmet.hizmetGorDur = true
            }


            binding.duzenleBtn.setOnClickListener {

                val hizmetAd= binding.hizmetAdi.text.toString().toLowerCase()
                val hizAciklama= binding.aciklama.text.toString()
                val hizFiyat=binding.fiyat.text.toString().replace(Regex("[^a-zA-Z0-9\\s]"), "").toInt()




  
                val hizmet = Hizmet(hizmet.firmaKodu,hizmet.hizmetId,hizmetAd,hizAciklama,hizFiyat, hizmet.hizmetGorDur)

                AppUtil.hizmetKaydet(hizmet){
                    if (it){
                        AppUtil.longToast(context,"Hizmet düzenlendi.")

                        findNavController().popBackStack()
                        findNavController().navigate(R.id.hizmetListeFragment)
                    }
                }
                if (hizmet.hizmetGorDur==false) AppUtil.longToast(requireContext(),"Hizmet randevularda görünmeyecek..")
         }
       }


        binding.layoutHizmAdi.setEndIconOnClickListener {
            askSpeechInput(121)
        }
        binding.layoutAciklama.setEndIconOnClickListener {
            askSpeechInput(122)
        }
       /* binding.layoutHizmFiyat.setEndIconOnClickListener {
            askSpeechInput(123)
        }*/

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ( resultCode== Activity.RESULT_OK){
            val result=data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            when (requestCode){
                121-> binding.hizmetAdi.setText(result?.get(0).toString())
                122-> binding.aciklama.setText(result?.get(0).toString())
               // 123-> binding.fiyat.setText(result?.get(0).toString())

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





    override fun getTheme(): Int {
        return R.style.hizmetDuzenleFragmentFullScreenDialogStyle

       }
}