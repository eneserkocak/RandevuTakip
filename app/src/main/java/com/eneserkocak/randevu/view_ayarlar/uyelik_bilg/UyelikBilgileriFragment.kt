package com.eneserkocak.randevu.view_ayarlar.uyelik_bilg

import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.ContentValues.TAG
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isNotEmpty
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.*
import com.eneserkocak.randevu.databinding.DialogUyelikBilgileriBinding
import com.eneserkocak.randevu.model.*
import com.eneserkocak.randevu.viewModel.AppViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


class UyelikBilgileriFragment : DialogFragment() {



    lateinit var binding: DialogUyelikBilgileriBinding
    val viewModel: AppViewModel by activityViewModels()

    lateinit var firma: Firma

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogUyelikBilgileriBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {




        //SKT ÜYELİK BİTİŞ TARİHİ
        FirebaseFirestore.getInstance().collection(FIRMALAR).document(UserUtil.firmaKodu.toString()).get().addOnSuccessListener {
              it?.let {

                  firma = it.toObject(Firma::class.java)!!


                  val firmaSkt = firma?.skt

                      val skt= firmaSkt!!.toDate()

                  val sdf = SimpleDateFormat("dd.MM.yyyy")
                  val uyelikBitis = sdf.format(skt)
                  binding.sktText.setText(uyelikBitis)




                  val cal= Calendar.getInstance()
                  val tarih= cal.time.toTimestamp()
                  val bugunTarih= tarih.toDate()



                  val tarihFarki= (skt.time-bugunTarih.time)/(1000*60*60*24)
                  val kalanGun=tarihFarki.toString()
                  binding.kalanGunText.setText(kalanGun)
                  println("GIRISTE TARİH ALACAZ: ${tarihFarki}")

              }
          }

        if (UserUtil.firmaKodu==100){
            binding.parolaDegistirBtn.visibility=View.GONE
        }else{
            binding.parolaDegistirBtn.visibility=View.VISIBLE
        }


        binding.switchBtn.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked){
                binding.parolaLayout.visibility= View.VISIBLE
            }else{
                binding.parolaLayout.visibility= View.GONE
            }

        }

        binding.switchHesapBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                binding.hesapLayout.visibility= View.VISIBLE
            }else{
                binding.hesapLayout.visibility= View.GONE
            }
        }

        binding.switchSktBtn.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked){
                binding.sktLayout.visibility= View.VISIBLE
            }else{
                binding.sktLayout.visibility= View.GONE
            }
        }



       binding.parolaDegistirBtn.setOnClickListener {

            sifreDegistirDialog()
        }

        binding.parolaText.doOnTextChanged { text, start, before, count ->

            if (text!!.length<6){
                binding.layoutParola.error = "En az 6 karakter giriniz..!"
            }else if(text!!.length==6 || text!!.length>6) {
                binding.layoutParola.error= null
            }

        }

            binding.IbanKopyalaBtn.setOnClickListener {
            val textToCopy = binding.ibanText.text.toString()

            val clipboardManager = activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", textToCopy)
            clipboardManager.setPrimaryClip(clipData)
            AppUtil.longToast(requireContext(),"IBAN numarası kopyalandı..")
            }








    /*binding.layoutParola.setEndIconOnClickListener {
        askSpeechInput(140)
    }*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ( resultCode== Activity.RESULT_OK){
            val result=data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            when (requestCode){

                140-> binding.parolaText.setText(result?.get(0).toString().filterNot { it.isWhitespace() })
            }
        }
    }
   /* private fun askSpeechInput(requestCode:Int) {
        if (!SpeechRecognizer.isRecognitionAvailable(requireContext())){
            AppUtil.longToast(requireContext(),"Konuşma tanıma kullanılamıyor..!")
        }else{
            val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Kaydetmek istediğinizi söyleyin..!")
            startActivityForResult(i,requestCode)
        }
    }*/

    override fun getTheme(): Int {
        return R.style.uyelikBilgileriFragmentFullScreenDialogStyle
    }

    fun sifreDegistirDialog(){

        val alert = AlertDialog.Builder(context)
        alert.setMessage("Giriş parolanız değişecek.   Onaylıyormusunuz ?")

            alert.setPositiveButton("EVET", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {

                    /*FirebaseFirestore.getInstance().collection(FIRMALAR).document(UserUtil.firmaKodu.toString())
                        .get()
                        .addOnSuccessListener {
                            val firma= it.toObject(Firma::class.java)
                            firma?.let {
                                val mevcutSifre= firma.sifre
                                println("MEVCUT SIFRE: ${mevcutSifre}")
                            }
                        }*/




                    val sifre= binding.parolaText.text.toString()

                    if (sifre.isNotEmpty() && sifre.length>=6){

                    val firmaMap = mapOf<String,Any>(

                        SIFRE to sifre
                    )
                    FirebaseFirestore.getInstance().collection(FIRMALAR).document(UserUtil.firmaKodu.toString())
                        .update(firmaMap)
                        .addOnSuccessListener {
                            firma.sifre=sifre

                        }

                  val auth= FirebaseAuth.getInstance()
                    FirebaseFirestore.getInstance().collection(FIRMALAR).document(UserUtil.firmaKodu.toString())
                        .collection(KULLANICILAR).document(auth.currentUser!!.uid)
                        .update(firmaMap)
                        .addOnSuccessListener {
                            firma.sifre=sifre
                        }

                    val user = Firebase.auth.currentUser
                    val newPassword = sifre

                    user!!.updatePassword(newPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "User password updated.")
                            }
                        }


                    AppUtil.longToast(requireContext(),"Parolanız değiştirildi.")
                    if (dialog != null) {
                        dialog.dismiss()
                    }
                  }







                    else{
                        AppUtil.longToast(requireContext(),"Parolanız en az 6 karakter olmalıdır..")
                    }


                }
            })
            alert.setNegativeButton("HAYIR", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    if (dialog != null) {
                        dialog.dismiss()
                    }
                }
            })

        alert.show()
    }








}