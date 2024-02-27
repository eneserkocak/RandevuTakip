package com.eneserkocak.randevu.view_ayarlar.personel_ayar

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
import com.eneserkocak.randevu.databinding.DialogHizmetEkleBinding
import com.eneserkocak.randevu.databinding.DialogPersonelEkleBinding

import com.eneserkocak.randevu.model.*
import com.eneserkocak.randevu.view.BaseFragment
import com.eneserkocak.randevu.viewModel.AppViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

class YeniPersonelEkleFragment : DialogFragment() {

    lateinit var binding: DialogPersonelEkleBinding
    val viewModel: AppViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogPersonelEkleBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    binding.personelKydt.setOnClickListener {

        val isim = binding.persIsmi.text.toString().toLowerCase()
        val tel = binding.persTel.text.toString().filterNot { it.isWhitespace() }
        val mail = binding.persMail.text.toString()

        if (isim.isEmpty() || tel.isEmpty()){
            Toast.makeText(requireContext()," İsim ve telefon numarası girilmeden personel eklenemez.!",Toast.LENGTH_LONG).show()
            return@setOnClickListener
        }




        FirebaseFirestore.getInstance().collection(PERSONELLER)
            .whereEqualTo(FIRMA_KODU,UserUtil.firmaKodu)
            .orderBy(PERSONEL_ID,Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener {
                it?.let {
                    var yeniPersonelId = 0

                    if (it.isEmpty) yeniPersonelId= 1
                    else {
                        for (document in it) {
                            val sonPersonel = document.toObject(Personel::class.java)
                            yeniPersonelId = sonPersonel.personelId+1

                        }
                    }
                    val personel = Personel(UserUtil.firmaKodu, yeniPersonelId, isim, tel, mail,)
                    viewModel.secilenPersonel.value = personel
                    println(personel)
                    personelEkle(personel)

                }
            }.addOnFailureListener {
                it.printStackTrace()
            }


       // PersonelDatabase.getInstance(requireContext())?.personelDao()?.insertAll(eklenenPersonel)



       }

        binding.layoutPersAdi.setEndIconOnClickListener {
            askSpeechInput( 110)
        }
        binding.layoutPersTel.setEndIconOnClickListener {
            askSpeechInput(111)
        }
        binding.layoutPersEposta.setEndIconOnClickListener {
            askSpeechInput(112)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ( resultCode== Activity.RESULT_OK){
            val result=data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            when (requestCode){
                110-> binding.persIsmi.setText(result?.get(0).toString())
                111-> binding.persTel.setText(result?.get(0).toString().filterNot { it.isWhitespace() })
                112-> binding.persMail.setText(result?.get(0).toString().filterNot { it.isWhitespace() })
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

    fun personelEkle(personel:Personel) {
            AppUtil.personelKaydet(personel) {
                if(it) {
                    Toast.makeText(requireContext(), "Yeni Personel Eklendi", Toast.LENGTH_LONG).show()

                    findNavController().popBackStack()
                    findNavController().navigate(R.id.personelListeFragment)
                } else
                    Toast.makeText(requireContext(), "HATA", Toast.LENGTH_LONG).show()
            }
    }

    override fun getTheme(): Int {
        return R.style.personelEkleFragmentFullScreenDialogStyle

    }

}