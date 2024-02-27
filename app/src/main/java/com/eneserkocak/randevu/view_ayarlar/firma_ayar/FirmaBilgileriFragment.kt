package com.eneserkocak.randevu.view_ayarlar.firma_ayar

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.Util.PictureUtil
import com.eneserkocak.randevu.Util.UserUtil

import com.eneserkocak.randevu.databinding.FragmentFirmaBilgileriBinding
import com.eneserkocak.randevu.model.*
import com.eneserkocak.randevu.view.BaseFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.*


class FirmaBilgileriFragment : BaseFragment<FragmentFirmaBilgileriBinding>(R.layout.fragment_firma_bilgileri) {

    private lateinit var activityResaultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    var selectedBitmap: Bitmap?=null
    lateinit var firmaLogo: String
    private var selectedPicture : Uri? =null
    private lateinit var storage: FirebaseStorage
    private var downloadUrl:String=""
    lateinit var girilenFirma: Firma



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerLauncher()

        PictureUtil.firmaGorselIndir(requireContext(),binding.imageView)

        val firmaKodu=UserUtil.firmaKodu.toString()
        binding.firmaKodu.setText(firmaKodu)


        FirebaseFirestore.getInstance().collection(FIRMALAR).document(UserUtil.firmaKodu.toString())
            .get()
            .addOnSuccessListener {
                it?.let {
                    val secilenFirma=it.toObject(Firma::class.java)

                    if (secilenFirma != null) {
                       // println("FIRMA BILGILER: ${secilenFirma.firmaAdi}")
                        binding.firmaIsmi.setText(secilenFirma.firmaAdi)
                        binding.telefonNo.setText(secilenFirma.firmaTel)
                        binding.adres.setText(secilenFirma.firmaAdres)
                    }
                }
            }



        binding.gorselEkleBtn.setOnClickListener {

           gorselIzin()
        }

        binding.firmaAyarKaydetBtn.setOnClickListener {

            val imageName= "${UserUtil.firmaKodu}.jpg"

            storage= Firebase.storage
            val reference= storage.reference

            val imageReference= reference.child("Firma_image").child(imageName)


            if (selectedPicture != null){
                imageReference.putFile(selectedPicture!!).addOnSuccessListener {
                    val uploadPictureRef= storage.reference.child("Firma_image").child(imageName)
                    uploadPictureRef.downloadUrl.addOnSuccessListener {
                        downloadUrl= it.toString()


                        println("URL: ${downloadUrl}")

                    }
                }.addOnFailureListener{
                    AppUtil.longToast(requireContext(),it.localizedMessage)
                }
            }

            val firmaAdi=binding.firmaIsmi.text.toString()
            val telNo= binding.telefonNo.text.toString().filterNot { it.isWhitespace() }
            val adres= binding.adres.text.toString()

             girilenFirma = Firma(UserUtil.firmaKodu,"","",firmaAdi,telNo,adres)



            val firmaMap = mapOf<String,Any>(
                FIRMA_ADI to firmaAdi,
                FIRMA_TEL to telNo,
                FIRMA_ADRES to adres
            )

            FirebaseFirestore.getInstance().collection(FIRMALAR).document(UserUtil.firmaKodu.toString())
                .update(firmaMap)
                .addOnSuccessListener {
                    AppUtil.longToast(requireContext(),"Firma Bilgileri Kaydedildi.")
                }


            /*  FirebaseFirestore.getInstance().collection(FIRMALAR).document(AppUtil.firmaDocumentPath(girilenFirma))
               *//* .collection(KULLANICILAR)
                .document(AppUtil.secondFirmaDocumentPath(girilenFirma))*//*
                .update(firmaMap)
                .addOnSuccessListener {

                    AppUtil.longToast(requireContext(),"Firma Bilgileri Kaydedildi.")
                    viewModel.kaydedilenAuthFirma.value=girilenFirma

                }*/


          /*  val sharedPreferences = AppUtil.getSharedPreferences(requireContext())

            sharedPreferences.edit().putString(FIRMA_ADI,firmaAdi).apply()
            sharedPreferences.edit().putString(FIRMA_TEL,telNo).apply()
            sharedPreferences.edit().putString(FIRMA_ADRES,adres).apply()*/


           // firmaDao.insert(firma)
            findNavController().navigate(R.id.girisFragment)
   }

        binding.layoutFirmaAdi.setEndIconOnClickListener {
            askSpeechInput( 102)
        }
        binding.layoutTel.setEndIconOnClickListener {
            askSpeechInput(103)
        }
        binding.layoutAdres.setEndIconOnClickListener {
            askSpeechInput(104)
        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ( resultCode==Activity.RESULT_OK){
            val result=data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            when (requestCode){
                102-> binding.firmaIsmi.setText(result?.get(0).toString())
                103-> binding.telefonNo.setText(result?.get(0).toString().filterNot { it.isWhitespace() })
                104-> binding.adres.setText(result?.get(0).toString())
            }
        }
    }
    private fun askSpeechInput(requestCode:Int) {
        if (!SpeechRecognizer.isRecognitionAvailable(requireContext())){
            AppUtil.longToast(requireContext(),"Konuşma tanıma kullanılamıyor..!")
        }else{
            val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Kaydetmek istediğinizi söyleyin..!")
            startActivityForResult(i,requestCode)
        }
    }

    private fun gorselIzin(){
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
            //ANDROİD 33+ üstü -> READ MEDIA IMAGES izni İSTENECEK
            if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_MEDIA_IMAGES ) != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_MEDIA_IMAGES )){
                    Snackbar.make(binding.root,"Permission needed for gallery!", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }.show()
                }else {
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                } }else{

                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

                activityResaultLauncher.launch(intentToGallery)
            }

        }else{
            //ANDROİD 32- altı -> READ EXTERNAL STORAGE izni İSTENECEK
            if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE )){
                    Snackbar.make(binding.root,"Permission needed for gallery!", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }.show()
                }else {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                } }else{

                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

                activityResaultLauncher.launch(intentToGallery)
            }
        }
    }
    private fun registerLauncher(){

        activityResaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if (result.resultCode == AppCompatActivity.RESULT_OK){
                val intentFromResult = result.data
                if (intentFromResult != null){
                    selectedPicture = intentFromResult.data
                    selectedPicture.let {
                        it.toString()
                       // binding.imageView.setImageURI(it)
  //Görseli ilk galeriden çekerken dönmesini engellemek için "setImageURI(it)" iptal Glide ı kullan:
                        Glide.with(this).load(it).into(binding.imageView)
                    }
                }
            }
        }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result->
            if (result){
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResaultLauncher.launch(intentToGallery)
            }else{
                Toast.makeText(requireContext(),"Permission needed!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun kucukBitmapOlustur(image:Bitmap,maximumSize: Int): Bitmap{

        var width= image.width
        var height= image.height

        val bitmapRatio: Double = width.toDouble() / height.toDouble()

        if (bitmapRatio<1){
            //landscape (Görsel Yatay)
        width=maximumSize
            val scaledHeight= width/bitmapRatio
            height=scaledHeight.toInt()

        }else{
            //portrait (Görsel Dikey)
            height=maximumSize
            val scaledWidth= height*bitmapRatio
            width=scaledWidth.toInt()
        }
        return Bitmap.createScaledBitmap(image,width,height,true)
     }

//AŞAĞIDAKİ REGİSTER LAUNCHER -->>BİTMAP İÇİN ---FİREBASE İ KULLANACAĞIM İÇİN YUKARDAKİNİ KULLAN
    /*private fun registerLauncher() {
     activityResaultLauncher =registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    val intentFromResult = result.data

                    if (intentFromResult != null) {

                        val imageData = intentFromResult.data
                        //binding.imageView.setImageURI(imageData)
                        if (imageData !=null){
                            try {
                                if (Build.VERSION.SDK_INT >=28){
                            val source= ImageDecoder.createSource(requireActivity().contentResolver,imageData)
                                selectedBitmap= ImageDecoder.decodeBitmap(source)
                                    binding.imageView.setImageBitmap(selectedBitmap)
                                }else{
                                    selectedBitmap=MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,imageData)
                                    binding.imageView.setImageBitmap(selectedBitmap)
                                }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                     }
                   }
                }
     }

     permissionLauncher =registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->

                        if (result) {

                            val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

                            activityResaultLauncher.launch(intentToGallery)

                        } else {Toast.makeText(requireContext(),"Permission needed!",Toast.LENGTH_LONG).show()}

                    }

            }*/


    //GÖRSEL İ BİTMAP E CEVİRDİM.BYTE DİZİSİNE CEVİRİP SHARED PREF İLE KAYDEDİP ALACAKTIM.ANCAK FİREBASE LE YAPTIM
    /*if (selectedBitmap !=null){

        val kucukBitmap= kucukBitmapOlustur(selectedBitmap!!,300)
        println(kucukBitmap)
        //görsel i byte dizisine çevirme:
        val outputStream= ByteArrayOutputStream()
        kucukBitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream)
         val byteArray= outputStream.toByteArray()

        //BYTE ARRAY i aldık..Bunu Base 64 ü kullnarak String e cevirim shared ile kaydet
        //Sonra aldığın yerde tekrar Base64 ü kullanarak bİTMAP A döndür AMA NASILLLL???

     //   val string = Base64.encodeToString(byteArray, Base64.DEFAULT)

    }*/


}
