package com.eneserkocak.randevu.view

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil

import com.eneserkocak.randevu.Util.UserUtil
import com.eneserkocak.randevu.Util.toTimestamp
import com.eneserkocak.randevu.databinding.FragmentAuthenticationBinding
import com.eneserkocak.randevu.model.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


class AuthenticationFragment:BaseFragment<FragmentAuthenticationBinding>(R.layout.fragment_authentication) {

    //Runnable Kodlar
    var textArray=ArrayList<TextView>()
    var runnable=Runnable{}
    val handler= Handler(Looper.getMainLooper())

    private lateinit var auth :FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.slaytText.isSelected=true

        auth= FirebaseAuth.getInstance()

        val currentUser= auth.currentUser
        val uid = currentUser?.uid
        if (currentUser != null){


            val sharedPref = AppUtil.getSharedPreferences(requireContext())
            val firmaKoduAl = sharedPref.getInt(FIRMA_KODU, 0)
            UserUtil.firmaKodu =firmaKoduAl


            findNavController().popBackStack()
        findNavController().navigate(R.id.girisFragment)
     }

        if (UserUtil.firmaKodu==101) binding.kayitOlBtn.visibility=View.VISIBLE
        else binding.kayitOlBtn.visibility=View.GONE

        binding.kayitOlBtn.setOnClickListener {

            val cal = Calendar.getInstance()
            val skt= cal.time.toTimestamp()

            val email= binding.epostaText.text.toString().trim()
            val password= binding.parolaText.text.toString()


            if (email.equals("") || password.equals("")){
                Toast.makeText(requireContext(),"E mail adresi ve şifrenizi giriniz..!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }


            FirebaseFirestore.getInstance().collection(FIRMALAR)
                .get()
                .addOnSuccessListener {
                    it?.let {
                        var yeniFirmaKodu = 0

                        if (it.isEmpty) yeniFirmaKodu= 100
                        else {
                            for (document in it) {
                                val sonFirma = document.toObject(Firma::class.java)
                                yeniFirmaKodu = sonFirma.firmaKodu+1

                                UserUtil.firmaKodu=yeniFirmaKodu


                            }
                        }
                    }
                }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful){

                    val uid=task.result.user!!.uid

          //FİRMA KAYDEDİLİRKEN İLK ALINAN "UID" aşağıda UserUtil deki ID yi eşitle.
          //UYGULAMADA HER YERDE AŞAĞIDA Kİ "USERUTİL.uid" yi ÇAĞIRIP KULLAN
                    UserUtil.uid=uid

            val kullanicilarArray=ArrayList<String>()
                    kullanicilarArray.add(uid)


               val yeniFirma= hashMapOf<String,Any>()

                yeniFirma.put(FIRMA_KODU,UserUtil.firmaKodu)
                yeniFirma.put(EMAIL,email)
                yeniFirma.put(SIFRE,password)
                yeniFirma.put(KULLANICILAR,kullanicilarArray)
                yeniFirma.put(SKT,skt)

                FirebaseFirestore.getInstance().collection(FIRMALAR).document(UserUtil.firmaKodu.toString())
                                .set(yeniFirma)




                      val yeniKullanici= hashMapOf<String,Any>()

                      yeniKullanici.put(UID,uid)
                      yeniKullanici.put(SIFRE,password)
                      yeniKullanici.put(EMAIL,email)
                 yeniKullanici.put(PATRON,true)


                FirebaseFirestore.getInstance().collection(FIRMALAR).document(UserUtil.firmaKodu.toString())
                                  .collection(KULLANICILAR).document(uid).set(yeniKullanici)
                     .addOnSuccessListener {
                         AppUtil.longToast(requireContext(),"Yeni firma eklendi")
                         findNavController().popBackStack()
                         findNavController().navigate(R.id.girisFragment)
                     }


                }else{
                    AppUtil.longToast(requireContext(),task.exception.toString())
                }
            }

        }

        binding.girisYapBtn.setOnClickListener {

            val cal = Calendar.getInstance()
            val tarih= cal.time.toTimestamp()

            val email= binding.epostaText.text.toString().trim()
            val password= binding.parolaText.text.toString()


            if (email.equals("") || password.equals("")){
                Toast.makeText(requireContext(),"İşletmeniz için tanımlanan e mail ve şifrenizi giriniz..!", Toast.LENGTH_LONG).show()
            }else{
                auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {

                    val userId=auth.currentUser?.uid

            FirebaseFirestore.getInstance().collection(FIRMALAR).whereArrayContains("kullanicilar",userId!!)
                .get(Source.SERVER)
                .addOnSuccessListener { document->


                    if (document.isEmpty || document ==null){
                        AppUtil.longToast(requireContext(),"Sisteme giriş yetkiniz yoktur..!")
                        auth.signOut()
                     //   findNavController().navigate(R.id.authenticationFragment)
                        return@addOnSuccessListener
                    }

               FirebaseFirestore.getInstance().collection(FIRMALAR).document(UserUtil.firmaKodu.toString())
                .get()
                .addOnSuccessListener {
                    it?.let {

                        val firma = it.toObject(Firma::class.java)

                        firma?.let {
                        val skt = it.skt

                       if (skt < tarih){

//                    AppUtil.longToast(requireContext(),"Üyelik süreniz dolmuştur..!")
        //                   findNavController().navigate(R.id.authenticationFragment)
                       auth.signOut()
                           findNavController().navigate(R.id.authenticationFragment)

                    }
                  }

                    }

                }


                    for (firma in document){
                        val firmaKodu = firma.get("firmaKodu").toString().toInt()

                        val sharedPreferences = AppUtil.getSharedPreferences(requireContext())
                        sharedPreferences.edit().putInt(FIRMA_KODU,firmaKodu).apply()
                        UserUtil.firmaKodu = firmaKodu

                    }

                    findNavController().popBackStack()
                    findNavController().navigate(R.id.girisFragment)

                  }
                }.addOnFailureListener {
                    Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()

                }
            }
        }

        binding.demoYoneticiGiris.setOnClickListener {


            val email = "alper@gmail.com"
            val password = "111111"

            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {

                val userId="J3uNXR5uZdNQuZ77qadCEhFH8FA2"
                //UserUtil.firmaKodu = 100

                FirebaseFirestore.getInstance().collection(FIRMALAR).whereArrayContains("kullanicilar",userId!!)
                    .get(Source.SERVER)
                    .addOnSuccessListener { document->

                        if (document.isEmpty || document ==null){
                            AppUtil.longToast(requireContext(),"Sisteme giriş yetkiniz yoktur..!")
                            auth.signOut()
                            return@addOnSuccessListener
                        }


                        UserUtil.firmaKodu = 100

                        findNavController().popBackStack()
                        findNavController().navigate(R.id.girisFragment)

                    }

            }.addOnFailureListener {
                Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()

            }

        }

        binding.demoPersGiris.setOnClickListener {


            val email = "kocak@hotmail.com"
            val password = "111111"

            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {

                val userId="iV6ApAi2sEPXxi2HiIpEDbMixHI3"
                //UserUtil.firmaKodu = 100

                FirebaseFirestore.getInstance().collection(FIRMALAR).whereArrayContains("kullanicilar",userId!!)
                    .get(Source.SERVER)
                    .addOnSuccessListener { document->

                        if (document.isEmpty || document ==null){
                            AppUtil.longToast(requireContext(),"Sisteme giriş yetkiniz yoktur..!")
                            auth.signOut()
                            return@addOnSuccessListener
                        }


                        UserUtil.firmaKodu = 100

                        findNavController().popBackStack()
                        findNavController().navigate(R.id.girisFragment)

                    }

            }.addOnFailureListener {
                Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()

            }

        }

        binding.whatsappBt.setOnClickListener {

            val yoneticiTel = "+90${"05062352416"}"

            val url = "https://api.whatsapp.com/send?phone=$yoneticiTel"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }




        //ANİMASYON KODLAR:
        val animasyon1 = AnimationUtils.loadAnimation(getContext(), R.anim.animasyon1)
        val animasyon2 = AnimationUtils.loadAnimation(getContext(), R.anim.animasyon2)

        val imageView2= binding.imageView2
        val imageView1= binding.imageView1

        imageView2.animation= animasyon1
        imageView1.animation=animasyon2



        //RUNNABLE TEXT LER
        textArray.add(binding.textView26)
        textArray.add(binding.textView27)
        textArray.add(binding.textView28)
        textArray.add(binding.textView29)
        textArray.add(binding.textView30)
        textArray.add(binding.textView31)
        textArray.add(binding.textView32)
        textArray.add(binding.textView33)
        textArray.add(binding.textView34)
        textArray.add(binding.textView35)
        textArray.add(binding.textView36)
        textArray.add(binding.textView37)
        textArray.add(binding.textView38)
        textArray.add(binding.textView39)
        textArray.add(binding.textView40)
        textArray.add(binding.textView41)
        textArray.add(binding.textView42)

        hideTexts()


     }
    fun hideTexts(){

        runnable=object :Runnable{
            override fun run() {
                for (text in textArray){

                    text.visibility=View.GONE

                }

                val random= Random()
                val randomIndex= random.nextInt(17)
                textArray[randomIndex].visibility=View.VISIBLE

                handler.postDelayed(runnable,1000)

                

            }

        }
        handler.post(runnable)

    }


    fun firmalarEkle(firma: Firma) {
        AppUtil.firmalarKaydet(firma) {
            if(it) {
                Toast.makeText(requireContext(), "Firma kaydı başarılı", Toast.LENGTH_LONG).show()

              findNavController().popBackStack()
               navigate(R.id.girisFragment)
            } else
                Toast.makeText(requireContext(), "HATA", Toast.LENGTH_LONG).show()
        }
    }


 }


    //KAYIT ESKİ YÖNTEM (APP UTİL DEN)
/*    binding.kayitOlBtn.setOnClickListener {

              val email= binding.epostaText.text.toString().trim()
              val password= binding.parolaText.text.toString()



          if (email.equals("") || password.equals("")){
              Toast.makeText(requireContext(),"E mail adresi ve şifrenizi giriniz..!",Toast.LENGTH_LONG).show()

          }else{

              auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {

                  val uid= it.user?.uid


                  FirebaseFirestore.getInstance().collection(FIRMALAR)
                      .orderBy(FIRMA_KODU, Query.Direction.DESCENDING)
                      .limit(1)

                      .get()
                      .addOnSuccessListener {
                          it?.let {
                              var yeniFirmaKodu = 0

                              if (it.isEmpty) yeniFirmaKodu= 100
                              else {
                                  for (document in it) {
                                      val sonFirma = document.toObject(Firma::class.java)
                                      yeniFirmaKodu = sonFirma.firmaKodu+1

                                  }
                              }
                               uid?.let {
                                  val firma = Firma(yeniFirmaKodu,email,password)
                                  viewModel.kaydedilenAuthFirma.value = firma
                                  println(firma)
                                  firmalarEkle(firma)

                                  UserUtil.firmaKodu.value=firma
                               }


             *//*
              val sharedPreferences = AppUtil.getSharedPreferences(requireContext())
               sharedPreferences.edit().putInt(FIRMA_KODU,yeniFirmaKodu).apply()
               sharedPreferences.edit().putString(FIRMA_ID,uid).apply()*//*
                           }
                       }.addOnFailureListener {
                           it.printStackTrace()
                       }
               }
           }
       }*/




