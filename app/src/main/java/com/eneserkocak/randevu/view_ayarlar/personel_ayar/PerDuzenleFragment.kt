package com.eneserkocak.randevu.view_ayarlar.personel_ayar

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
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



         /*  binding.apply {
               persIsmi.setText(it.personelAdi.toString())
               persTel.setText(it.personelTel.toString())
              persMail.setText(it.personelMail.toString())
              persUnvan.setText(persUnvan.text.toString())
           }*/




      binding.personelGnclle.setOnClickListener {

          //Görsel Storage kaydetme işlemi:
          //val uuid= UUID.randomUUID()
         // val imageName= "$uuid.jpg"


          val imageName= "${personel.personelId}.jpg"

          storage= Firebase.storage
          val reference= storage.reference

          val imageReference= reference.child("Personel_images").child(imageName)



          //Burada Storage a kaydedilen Görselin URL (download Url) sini alıp-> Firestora kaydedeceğiz.:
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
          val isim = binding.persIsmi.text.toString()
          val tel = binding.persTel.text.toString()
          val mail = binding.persMail.text.toString()
          val unvan = binding.persUnvan.text.toString()



          val personel = Personel(personel.firmaId,personel.personelId,isim,tel,mail,unvan)


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