package com.eneserkocak.randevu.view_destek

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.databinding.FragmentDestekBinding
import com.eneserkocak.randevu.view.BaseFragment
import java.util.*


class DestekFragment : BaseFragment<FragmentDestekBinding>(R.layout.fragment_destek) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



       binding.gonderBtn.setOnClickListener {


           val email="huseyinerkocak@hotmail.com"
           val mesaj=binding.mailDestek.text.toString()

          sendEmail(email,mesaj)
       }
      // AppUtil.longToast(requireContext(),"İletişime geçtiğiniz için teşekkür ederiz..Mail adresinize dönüş yapılacaktır..")




        binding.layoutDestek.setEndIconOnClickListener {
            askSpeechInput(127)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ( resultCode== Activity.RESULT_OK){
            val result=data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            when (requestCode){
                127-> binding.mailDestek.setText(result?.get(0).toString())



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
    fun sendEmail(email: String, mesaj: String) {

        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        mIntent.putExtra(Intent.EXTRA_TEXT, mesaj)


        try {
            //start email intent
            startActivity(Intent.createChooser(mIntent, "E-MAİL UYGULAMANIZI SEÇİNİZ..!"))

        }
        catch (e: Exception){
            e.printStackTrace()
        }

    }
}