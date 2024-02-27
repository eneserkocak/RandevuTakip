package com.eneserkocak.randevu.view_randevuListesi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
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
import com.eneserkocak.randevu.databinding.DialogRandevuNotlarBinding
import com.eneserkocak.randevu.model.*
import com.eneserkocak.randevu.viewModel.AppViewModel
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class RandevuNotlarFragment : DialogFragment()  {


    lateinit var binding: DialogRandevuNotlarBinding
    val viewModel: AppViewModel by activityViewModels()

    lateinit var randevu: Randevu


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogRandevuNotlarBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel= viewModel

        if (binding.randNotuText.text!!.isEmpty()) {
            binding.notEkleBtn.setText("Not Ekle")
        }else {binding.notEkleBtn.setText("Notu Düzenle")}

        viewModel.secilenRandevu.observe(viewLifecycleOwner){ secilenRandevu->
            secilenRandevu?.let {
                randevu=it


        binding.notEkleBtn.setOnClickListener {

            val randevuNotu = binding.randNotuText.text.toString()

            val randevuMap = mapOf<String,Any>(

                RANDEVU_NOTU to randevuNotu
            )


            FirebaseFirestore.getInstance().collection(RANDEVULAR).document(AppUtil.randevuDocumentPath(randevu))
                .update(randevuMap)


            dismiss()

            AppUtil.longToast(requireContext(),"Randevuya Not Eklendi..!")
        }



            }
        }

        binding.vazgecBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.layoutRandNot.setEndIconOnClickListener {
            askSpeechInput(126)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ( resultCode== Activity.RESULT_OK){
            val result=data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            when (requestCode){
                126-> binding.randNotuText.setText(result?.get(0).toString())

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
        return R.style.randevuNotlarFragmentFullScreenDialogStyle
    }

}