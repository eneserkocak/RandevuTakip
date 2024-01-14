package com.eneserkocak.randevu.view_musteri

import android.Manifest
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
import com.eneserkocak.randevu.databinding.FragmentYeniMusteriEkleBinding
import com.eneserkocak.randevu.model.FIRMA_ID
import com.eneserkocak.randevu.model.MUSTERILER
import com.eneserkocak.randevu.model.MUSTERI_ID
import com.eneserkocak.randevu.model.Musteri
import com.eneserkocak.randevu.view.BaseFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import io.grpc.Context.Storage
import java.util.UUID


class YeniMusteriEkleFragment : BaseFragment<FragmentYeniMusteriEkleBinding>(R.layout.fragment_yeni_musteri_ekle) {

    private lateinit var activityResaultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var selectedPicture : Uri? =null
    private lateinit var storage: FirebaseStorage
    private var downloadUrl: String=""

    lateinit var gelinenYer : String
    lateinit var musteri: Musteri


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerLauncher()

        arguments?.let {

            gelinenYer=YeniMusteriEkleFragmentArgs.fromBundle(it).gelinenSayfa
        }

        //EĞER MUSTERİ DUZENLE DEN GELİRSE BURAYI ÇALIŞTIR
        if (gelinenYer== MUSTERI_DUZENLE) {

            binding.musteriEkleBtn.setText("Bilgileri Güncelle")
            binding.gorselEkleBtn.setText("Görsel Ekle/Değiştir")

            viewModel.secilenMusteri.observe(viewLifecycleOwner) {  secilenMusteri->
                secilenMusteri?.let {
                    musteri=it
                    PictureUtil.gorselIndir(it,requireContext(),binding.imageView)

                binding.musteriIsmi.setText(it.musteriAdi)
                binding.musteriTel.setText(it.musteriTel)
                binding.musteriMail.setText(it.musteriMail)
                binding.musteriAdres.setText(it.musteriAdres)
                binding.musteriNot.setText(it.musteriNot)

                //Müşteri Düzenle Buton:
                binding.musteriEkleBtn.setOnClickListener {


                    val image="${musteri.musteriId}.jpg"


                    storage= Firebase.storage
                    val reference= storage.reference

                    val imageReference= reference.child("Musteri_images").child(image)
                    if (selectedPicture != null){
                        imageReference.putFile(selectedPicture!!).addOnSuccessListener {
                            val uploadPictureRef= storage.reference.child("Musteri_images").child(image)
                            uploadPictureRef.downloadUrl.addOnSuccessListener { uri->
                                uri?.let {
                                    downloadUrl= it.toString()

                                    println(downloadUrl)
                                }

                            }

                        }.addOnFailureListener{
                            AppUtil.longToast(requireContext(),it.localizedMessage)
                        }
                    }

                    val isim =binding.musteriIsmi.text.toString()
                    val tel =binding.musteriTel.text.toString()
                    val mail =binding.musteriMail.text.toString()
                    val adres =binding.musteriAdres.text.toString()
                    val not =binding.musteriNot.text.toString()

                    val musteri=Musteri(musteri.firmaId,musteri.musteriId,isim,tel,mail,adres,not)

                    AppUtil.musteriKaydet(musteri){
                        if (it){
                            AppUtil.longToast(context,"Müşteri bilgileri düzenlendi.")
                            findNavController().popBackStack()
                        }
                    }
                }
                    binding.gorselEkleBtn.setOnClickListener {
                        gorselIzin()
                    }

                binding.vazgecBtn.setOnClickListener {
                    findNavController().popBackStack()
                }
              }
            }
            return

        }

        //EĞER MUSTERİ LİSTE FRAGMENT TAN GELİRSE BURAYI ÇALIŞTIR(yeni must ekle)

        binding.musteriEkleBtn.setOnClickListener {

            viewModel.secilenMusteri.observe(viewLifecycleOwner){
                it?.let {
                    musteri=it
//YENİ MÜŞT EKLERKEN yukarıdaki gibi secilenMust yi observe edersek 2 adet görsel ekliyor..FAKAT
//AŞAĞIDA JPG ye VERECEĞİMİZ ID ->>>MUST DUZENLEDEKİ ID İLE AYNI OLMAK ZORUNDA
//FAKAT BURDA MÜŞTERİYİ YENİ OLUŞTURUYORUZ..ID Yİ NASIL VERECEĞİZZZ

            //Görsel Storage kaydetme işlemi:
            /*val uuid= UUID.randomUUID()
            val imageName= "$uuid.jpg"*/

            val image="${musteri.musteriId}.jpg"
            storage= Firebase.storage
            val reference= storage.reference
                    val imageReference= reference.child("Musteri_images").child(image)

                        if (selectedPicture != null){

                        imageReference.putFile(selectedPicture!!).addOnSuccessListener {
                            val uploadPictureRef= storage.reference.child("Musteri_images").child(image)
                            uploadPictureRef.downloadUrl.addOnSuccessListener { uri->
                                uri?.let {
                                    downloadUrl= it.toString()

                                    println(downloadUrl)
                                }

                            }

                        }.addOnFailureListener{
                            AppUtil.longToast(requireContext(),it.localizedMessage)
                        }
                    }
                }
            }

            val mustIsim = binding.musteriIsmi.text.toString()
            val mustTel = binding.musteriTel.text.toString()
            val mustMail = binding.musteriMail.text.toString()
            val mustAdres = binding.musteriAdres.text.toString()
            val mustNot = binding.musteriNot.text.toString()
            val mustGorsel= downloadUrl

            println(mustGorsel)

            if (mustIsim.isEmpty()) {

                Toast.makeText(requireContext(),"İsim girilmeden müşteri eklenemez.!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            FirebaseFirestore.getInstance().collection(MUSTERILER)
                .whereEqualTo(FIRMA_ID,1)
                .orderBy(MUSTERI_ID, Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener {
                    it?.let {
                        var yeniMusteriId=0
                        if (it.isEmpty) yeniMusteriId=1
                        else{
                            for (document in it) {
                                val sonMusteri = document.toObject(Musteri::class.java)
                                yeniMusteriId = sonMusteri.musteriId+1

                            }
                        }
                        val yeniMusteri= Musteri(1,yeniMusteriId,mustIsim,mustTel,mustMail,mustAdres,mustNot,mustGorsel)
                        viewModel.secilenMusteri.value=yeniMusteri
                        musteriEkle(yeniMusteri)


                    }
                }.addOnFailureListener {
                    it.printStackTrace()
                }



        }
        binding.vazgecBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.gorselEkleBtn.setOnClickListener {

            gorselIzin()
        }



    }

    fun musteriEkle(musteri: Musteri) {
        AppUtil.musteriKaydet(musteri) {
            if(it) {
                Toast.makeText(requireContext(), "Yeni Müşteri Eklendi", Toast.LENGTH_LONG).show()

                findNavController().popBackStack()
               navigate(R.id.musteriListeFragment)
            } else
                Toast.makeText(requireContext(), "HATA", Toast.LENGTH_LONG).show()
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
}