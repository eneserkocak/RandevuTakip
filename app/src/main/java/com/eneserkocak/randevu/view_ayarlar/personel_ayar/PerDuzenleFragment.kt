package com.eneserkocak.randevu.view_ayarlar.personel_ayar

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
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
import com.eneserkocak.randevu.databinding.FragmentPerDuzenleBinding
import com.eneserkocak.randevu.model.*
import com.eneserkocak.randevu.view.BaseFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.*


class PerDuzenleFragment : BaseFragment<FragmentPerDuzenleBinding>(R.layout.fragment_per_duzenle) {

    private lateinit var activityResaultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var selectedPicture : Uri? =null
    private lateinit var storage: FirebaseStorage
    private var downloadUrl:String=""
    lateinit var personel  :Personel

    //private var downloadUrl:Uri?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel= viewModel

        registerLauncher()



        viewModel.secilenPersonel.observe(viewLifecycleOwner){ secilenPersonel->
            secilenPersonel?.let {
                personel = it
                PictureUtil.gorseliAl(personel,requireContext(),binding.imageView)

                val checkedId = if (!personel.personelYetki) R.id.personelBtn else R.id.yoneticiBtn
                binding.RadioGrupPers.check(checkedId)

                binding.RadioGrupPers.setOnCheckedChangeListener { group, checkedId ->
                    if (R.id.yoneticiBtn == checkedId) personel.personelYetki = true
                    else personel.personelYetki = false
                }

                if (personel.personelHesap==true)
                    personel.personelHesap=true

         /*  binding.apply {
               persIsmi.setText(it.personelAdi.toString())
               persTel.setText(it.personelTel.toString())
              persMail.setText(it.personelMail.toString())
              persUnvan.setText(persUnvan.text.toString())
           }*/

      binding.personelGnclle.setOnClickListener {


          val imageName= "${personel.firmaKodu}-${personel.personelId}.jpg"

          storage= Firebase.storage
          val reference= storage.reference

          val imageReference= reference.child("Personel_images").child(imageName)

          if (selectedPicture != null){
              imageReference.putFile(selectedPicture!!).addOnSuccessListener {
                  val uploadPictureRef= storage.reference.child("Personel_images").child(imageName)
                  uploadPictureRef.downloadUrl.addOnSuccessListener {
                          downloadUrl= it.toString()


                          println("URL: ${downloadUrl}")

                  }
              }.addOnFailureListener{
                  AppUtil.longToast(requireContext(),it.localizedMessage)
        }
      }

          val isim = binding.persIsmi.text.toString().toLowerCase()
          val tel = binding.persTel.text.toString().filterNot { it.isWhitespace() }
          val mail = binding.persMail.text.toString()
          val unvan = binding.persUnvan.text.toString()


            val personel= Personel(personel.firmaKodu,personel.personelId,isim,tel,mail,unvan,personel.personelYetki,personel.personelHesap)



          AppUtil.personelKaydet(personel){
              if (it){
                  AppUtil.longToast(context,"Personel düzenlendi.")
                  findNavController().popBackStack()
             }
           }




        }
     }
   }
        binding.vazgecBtn.setOnClickListener {
            findNavController().popBackStack()

        }
        binding.gorselEkleBtn.setOnClickListener {

             gorselIzin()
        }

        binding.layoutPersAdi.setEndIconOnClickListener {
            askSpeechInput( 113)
        }
        binding.layoutPersTel.setEndIconOnClickListener {
            askSpeechInput(114)
        }
        binding.layoutPersEposta.setEndIconOnClickListener {
            askSpeechInput(115)
        }
        binding.layoutPersUnvan.setEndIconOnClickListener {
            askSpeechInput(116)
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ( resultCode== Activity.RESULT_OK){
            val result=data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            when (requestCode){
                113-> binding.persIsmi.setText(result?.get(0).toString())
                114-> binding.persTel.setText(result?.get(0).toString().filterNot { it.isWhitespace() })
                115-> binding.persMail.setText(result?.get(0).toString().filterNot { it.isWhitespace() })
                116-> binding.persUnvan.setText(result?.get(0).toString())
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
                     //   binding.imageView.setImageURI(it)
                    //Resimler ilk çekilirken dönmesini engellemek için Glide ı kullan yukardaki setImageUri iptal..
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
}


//PERSONEL SİLME Yİ PERS RANDEVULAR FRAGMENT A ALDIM
/*private fun personelSilDialog(){

    val alert =AlertDialog.Builder(requireContext())
    alert.setMessage("Personel silinsin mi?")

    alert.setPositiveButton("EVET", object :DialogInterface.OnClickListener{
        override fun onClick(dialog: DialogInterface?, which: Int) {
            //Firebase den silme işlemi yap
            AppUtil.personelSil(personel,{})
            PictureUtil.gorseliSil(personel,requireContext(),{})
            AppUtil.longToast(requireContext(),"personel silindi")
            findNavController().popBackStack()

        }
    })
    alert.setNegativeButton("HAYIR", object :DialogInterface.OnClickListener{
        override fun onClick(dialog: DialogInterface?, which: Int) {
            findNavController().popBackStack()
        }
    })
    alert.show()
}*/