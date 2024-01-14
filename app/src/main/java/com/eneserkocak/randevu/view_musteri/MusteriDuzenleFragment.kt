package com.eneserkocak.randevu.view_musteri

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isNotEmpty
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.Util.PictureUtil
import com.eneserkocak.randevu.adapter.MusteriDuzRandAdapter
import com.eneserkocak.randevu.databinding.DialogMusteriDuzenleBinding
import com.eneserkocak.randevu.db_firmaMaps.FirmaMapsDao
import com.eneserkocak.randevu.model.*
import com.eneserkocak.randevu.viewModel.AppViewModel


const val MUSTERI_DUZENLE = "MusteriDuzenle"



  var filtreRandevuListesi= listOf<Randevu>()
  //lateinit var filtreRandevuListesiLD : LiveData<List<Randevu>>

class MusteriDuzenleFragment : DialogFragment() {

    val adapter= MusteriDuzRandAdapter()



    lateinit var secMusteri:Musteri


    lateinit var binding:DialogMusteriDuzenleBinding
    val viewModel: AppViewModel by activityViewModels()

    lateinit var dao: FirmaMapsDao
   private var firmaKonumList= listOf<Konum>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogMusteriDuzenleBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //FİREBASE DEN MUSTERİ İSİMLERİNİ FİLTRELEYEREK BURADA RANDEVULARI ÇEKİP AŞAGIDA ADAPTERE VER:

        binding.mustDuznRandevuRecycler .layoutManager=LinearLayoutManager(requireContext())
        binding.mustDuznRandevuRecycler.adapter=adapter

       // adapter.musteriRandevuListesiniGuncelle(it)


        //Firma nın konumunu ROOM dan burada aldık: Tek bir Konum Kaydedeceğimiz için ROOM İPTAL->SHARED la YAPACAM
       // dao = FirmaMapsDatabase.getInstance(requireContext())!!.firmaMapsDao()
        //firmaKonumList = dao.getAll()


        viewModel.secilenMusteri.observe(viewLifecycleOwner){
            it?.let {
                secMusteri=it
                PictureUtil.gorselIndir(it,requireContext(),binding.musteriGorsel)


            }
        }


        viewModel.randVerileriGetir()
        viewModel.randevuListesi.observe(viewLifecycleOwner){
            it?.let {

                filtreRandevuListesi = it.filter { it.musteri.musteriAdi==secMusteri.musteriAdi}
              // filtreRandevuListesiLD= it.filter { it.musteri.musteriAdi==secMusteri.musteriAdi}
                //adapter.randevuListesiniGuncelle(it)

         if (filtreRandevuListesi.isNotEmpty()){
                binding.mustDuznRandevuRecycler.visibility=View.VISIBLE
                binding.musteriNotText.visibility=View.GONE
                adapter.musteriRandevuListesiniGuncelle(filtreRandevuListesi)
        }else{
                binding.mustDuznRandevuRecycler.visibility=View.GONE
                binding.musteriNotText.visibility=View.VISIBLE
        }
      }
   }


/*
        val sharedPref = AppUtil.getSharedPreferences(requireContext())
        val firmaAdiAl = sharedPref.getString(FIRMA_ISIM, "boş")
        var firmaLat= sharedPref.getString(FIRMA_LAT, "boş")
        var firmaLng = sharedPref.getString(FIRMA_LNG, "boş")

        val latitude = firmaLat!!
        val longitude= firmaLng!!*/

        binding.konumGndrBtn.setOnClickListener {

            findNavController().navigate(R.id.firmaKonumListeFragment)

        }
        binding.musteriSilBtn.setOnClickListener {


            musteriSilDialog()
        }

        binding.mustBlgDznlBtn.setOnClickListener {

            dismiss()
            val action = MusteriDuzenleFragmentDirections.actionMusteriDuzenleFragmentToYeniMusteriEkleFragment(MUSTERI_DUZENLE)
            findNavController().navigate(action)

        }



        viewModel.secilenMusteri.value?.let {
                    secMusteri=it

            binding.musteriAdiText.text= it.musteriAdi
            binding.musteriTelText.text= it.musteriTel
            binding.musteriMailText.text= it.musteriMail
            binding.musteriNotText.text=it.musteriNot
        }


          val clickListener : (ClickedButton)->Unit = {
          binding.clickedButton = it
        }



        binding.notlarBtn.setOnClickListener {
            clickListener.invoke(ClickedButton.Notlar)
            binding.musteriNotText.visibility=View.VISIBLE
            binding.mustDuznRandevuRecycler.visibility=View.GONE
        }

        binding.randevularBtn.setOnClickListener {
            clickListener.invoke(ClickedButton.Randevular)
            binding.musteriNotText.visibility=View.GONE

            if(binding.mustDuznRandevuRecycler.isNotEmpty()) {
                binding.mustDuznRandevuRecycler.visibility = View.VISIBLE
            }else{ binding.mustDuznRandevuRecycler.visibility=View.GONE }

            adapter.musteriRandevuListesiniGuncelle(filtreRandevuListesi)
        }


        checkPermissions()

        binding.mustAraBtn.setOnClickListener {

            musteriAraDialog()
        }

    }

    private fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CALL_PHONE),101)
        }
    }

    override fun getTheme(): Int {
        return R.style.musteriDuzenleFragmentFullScreenDialogStyle
    }

    fun musteriSilDialog(){

        val alert = AlertDialog.Builder(requireContext())
       // alert.setMessage("Müşteri silinsin mi?")

        val musteriAdi= secMusteri.musteriAdi.uppercase()
        if (binding.mustDuznRandevuRecycler.isNotEmpty()){
            alert.setMessage("${musteriAdi} isimli müşterinin kayıtlı geçmiş randevu bilgisi bulunmaktadır. Randevusu bulunan müşteriyi silemezsiniz...!           Müşteriye ait RANDEVULAR bölümünden, geçmiş randevularını sildikten sonra müşteriyi silebilirsiniz..")
            alert.setTitle("UYARI..!")
        }else{
            alert.setMessage("Müşteri silinsin mi?")


        alert.setPositiveButton("EVET", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                //Firebase den silme işlemi yap
                AppUtil.musteriSil(secMusteri,{})
                PictureUtil.musterigorseliSil(secMusteri,requireContext(),{})

                AppUtil.longToast(requireContext(),"Müşteri silindi")
                findNavController().popBackStack()


          //Aşğıdaki navigate i koymayınca dialog kapanıyor fakat liste fragment ta çık gir yapmadan müşt silinmiyor
                findNavController().navigate(R.id.musteriListeFragment)

            }
        })
        alert.setNegativeButton("HAYIR", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
               // findNavController().popBackStack()
            }
        })
    }
        alert.show()
    }

    fun musteriAraDialog(){

        val alert = AlertDialog.Builder(requireContext())
        alert.setMessage("Müşteri aransın mı?")

            alert.setPositiveButton("EVET", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {

                    var musteriTel= binding.musteriTelText.text.toString()
                    if (musteriTel.isNotEmpty()){
                        val callIntent= Intent(Intent.ACTION_CALL)
                        callIntent.data= Uri.parse("tel:$musteriTel")
                        startActivity(callIntent)
                    }else{
                        Toast.makeText(requireContext(),"Arama yapmak için müşteriye numara eklemelisiniz!..",Toast.LENGTH_LONG).show()
                    }

                    AppUtil.longToast(requireContext(),"Müşteri aranıyor..!")
                    findNavController().popBackStack()

                }
            })
            alert.setNegativeButton("HAYIR", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                   // findNavController().popBackStack()
                }
            })

        alert.show()
    }
}