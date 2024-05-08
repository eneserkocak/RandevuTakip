package com.eneserkocak.randevu.view_ayarlar.hesap_yonetimi

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isNotEmpty
import androidx.core.widget.doOnTextChanged
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.Util.PictureUtil
import com.eneserkocak.randevu.Util.UserUtil
import com.eneserkocak.randevu.databinding.FragmentPersHesapEkleBinding
import com.eneserkocak.randevu.model.*
import com.eneserkocak.randevu.view.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class PersHesapEkleFragment :BaseFragment<FragmentPersHesapEkleBinding>(R.layout.fragment_pers_hesap_ekle) {

    lateinit var secilenPersonel: Personel
    private lateinit var auth: FirebaseAuth


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        viewModel.secilenPersonel.observe(viewLifecycleOwner) {
            it?.let {
                secilenPersonel = it

                PictureUtil.gorseliAl(secilenPersonel, requireContext(), binding.imageView)

                binding.epostaText.setText(it.personelMail)

                if (it.personelHesap==true){
                    AppUtil.longToast(requireContext(),"Personele ait daha önce oluşturulan giriş hesabı mevcuttur. Yeni hesap oluşturulamaz..!!")
                    return@let
                }
            }
        }

        /*FirebaseFirestore.getInstance().collection(FIRMALAR).document(UserUtil.firmaKodu.toString())
            .get()
            .addOnSuccessListener {
                it?.let {
                    val firmaBilgi=it.toObject<Firma>()
                        firmaBilgi?.let {
                            val eposta=it.email
                            val firmaAdi=it.firmaAdi
                            val firmaAdres=it.firmaAdres
                            val firmaKodu=it.firmaKodu
                            val firmaTel=it.firmaTel
                            val sifre=it.sifre
                        }
                }
            }*/

        binding.kayitOlBtn.setOnClickListener {

            val email = binding.epostaText.text.toString().trim()
            val password = binding.parolaText.text.toString()
            val persAdi = secilenPersonel.personelAdi.toString()
            val persTel = secilenPersonel.personelTel.toString()

            if (email.equals("") || password.equals("")) {
                Toast.makeText(
                    requireContext(),
                    "Personele ait e mail adresi ve şifre giriniz..!",
                    Toast.LENGTH_LONG
                ).show()

            } else {

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            val uid = task.result.user!!.uid
                            FirebaseFirestore.getInstance().collection(FIRMALAR)
                                .document(UserUtil.firmaKodu.toString())
                                .get()
                                .addOnSuccessListener {
                                    it?.let {
                                        val kullanicilarArray =
                                            it.get("kullanicilar") as ArrayList<String>

                                       // kullanicilarArray.add(UserUtil.uid)
                                        //PERSONELLER UID si
                                        kullanicilarArray.add(uid)
                                        UserUtil.kullanicilar = kullanicilarArray


                                        FirebaseFirestore.getInstance().collection(FIRMALAR)
                                            .document(UserUtil.firmaKodu.toString())
                                            .update(mapOf( KULLANICILAR to kullanicilarArray))


                        //Burada yeniKullanici ismiyle bir class oluşturup..Patron sorgusunu Giriş Fragm. ta CLass tan da yapabilridik.
                                        val yeniKullanici = hashMapOf<String, Any>()

                                        yeniKullanici.put(UID, uid)
                                        yeniKullanici.put(SIFRE, password)
                                        yeniKullanici.put(EMAIL, email)
                                        yeniKullanici.put(PERSONEL_ADI, persAdi)
                                        yeniKullanici.put(PERSONEL_TEL, persTel)
                                        yeniKullanici.put(PATRON,false)




                                        FirebaseFirestore.getInstance().collection(FIRMALAR).document(UserUtil.firmaKodu.toString())
                                            .collection(KULLANICILAR).document(uid)
                                            .set(yeniKullanici)
                                            .addOnSuccessListener {
                                                AppUtil.longToast(requireContext(),"Personel kullanıcı hesabı oluşturuldu")

                                                val personelHesap= true
                                                val personelMap = mapOf<String,Any>(

                                                    PERSONEL_HESAP to personelHesap
                                                )
                                                FirebaseFirestore.getInstance().collection(PERSONELLER).document(AppUtil.documentPath(secilenPersonel))
                                                    .update(personelMap)
                                                    .addOnSuccessListener {
                                                       secilenPersonel.personelHesap =true

                                                    }


                                                findNavController().popBackStack()
                                                findNavController().navigate(R.id.ayarlarFragment)

                                                uyariDialog(requireActivity())
                              }
                          }
                      }
                  }
              }
          }
      }

        binding.parolaText.doOnTextChanged { text, start, before, count ->

            if (text!!.length<6){
                binding.layoutParola.error = "En az 6 karakter giriniz..!"
            }else if(text!!.length==6 || text!!.length>6) {
                binding.layoutParola.error= null
            }

        }

        binding.layoutEposta.setEndIconOnClickListener {
            askSpeechInput(128)
        }
       /* binding.layoutParola.setEndIconOnClickListener {
            askSpeechInput(129)
        }*/

  }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ( resultCode== Activity.RESULT_OK){
            val result=data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            when (requestCode){
                128-> binding.epostaText.setText(result?.get(0).toString())
              //  129-> binding.parolaText.setText(result?.get(0).toString().filterNot { it.isWhitespace() })
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

    fun uyariDialog(activity: Activity){

        val alert = AlertDialog.Builder(requireContext())


        alert.setMessage("${secilenPersonel.personelAdi.uppercase()} isimli personele açılan hesaba geçiş yaptınız...!                               Kendi hesabınıza dönmek için, mevcut oturumdan (Aşağıdan veya Anasayfadan) ÇIKIŞ yapıp, kendi bilgilerinizle hesabınıza giriş yapın..")
        alert.setTitle("UYARI..!")
        alert.setIcon(R.drawable.tum_ilac_ilacsil_icon)

            alert.setPositiveButton("ÇIKIŞ YAP", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    //Firebase den auth ÇIKIŞ işlemi yap
                    auth= FirebaseAuth.getInstance()
                    auth.signOut()
                    Navigation.findNavController(activity,R.id.fragmentContainerView).navigate(R.id.authenticationFragment)


                  //  findNavController().popBackStack()


                }
            })
            alert.setNegativeButton("İPTAL", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                  
                }
            })

        alert.show()
    }


 }



