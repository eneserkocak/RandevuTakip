package com.eneserkocak.randevu.view_randevuEkle

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.*
import com.eneserkocak.randevu.adapter.RandHizmGostAdapter

import com.eneserkocak.randevu.adapter.RandPersGostAdapter

import com.eneserkocak.randevu.adapter.RandSaatGosterAdapter

import com.eneserkocak.randevu.databinding.FragmentRandevuAlBinding
import com.eneserkocak.randevu.model.*
import com.eneserkocak.randevu.view.BaseFragment

import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import java.text.SimpleDateFormat
import java.util.*

const val RANDEVULAR = "randevular"
class RandevuAlFragment() : BaseFragment<FragmentRandevuAlBinding>(R.layout.fragment_randevu_al) {

    private var personel:Personel?=null
    lateinit var musteri: Musteri
    private var hizmet: Hizmet?=null



    var personelListesi= listOf<Personel>()
    val adapter = RandPersGostAdapter(){
        viewModel.secilenPersonel.value=it
        personel = it


    }


    var hizmetListesi= listOf<Hizmet>()
    val hizmetAdapter= RandHizmGostAdapter(){
        viewModel.secilenHizmet.value= it
        hizmet = it

    }

    private var secilenRandevuSaati: RandevuSaati? = null
    val saatAdapter= RandSaatGosterAdapter(){
        secilenRandevuSaati = it


    }

    lateinit var cal: Calendar

    //lateinit var dao: PersonelDao

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




// Ayarlarda personel, hizmet vs row lara tıklayınca ordaki tıklanan ları burada seçilen olarak otomatik alıyor
// Bunu engellemek için aşağıda seçilen değerleri NULL olarak başlat: FAKAT MUSTERİ OLMADI..(Müşteri Giriş Fragment ta yapıldı)
        viewModel.secilenPersonel.value=null
        viewModel.secilenHizmet.value=null


        binding.viewModel=viewModel


        viewModel.secilenMusteri.observe(viewLifecycleOwner){
            it?.let {

                musteri=it
                PictureUtil.gorselIndir(it,requireContext(),binding.musteriGorsel)

            }
        }


        //dao= PersonelDatabase.getInstance(requireContext())!!.personelDao()
        //personelList= dao.getAll()
        //adapter.personelListesiniGuncelle(personelList)

        getData(){
            personelListesi = it
            adapter.personelListesiniGuncelle(it)

        }

        takeData(){
            hizmetListesi = it
            hizmetAdapter.hizmetListGuncelle(it)
        }

        binding.randevuAlRecycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.randevuAlRecycler.adapter = adapter

        binding.hizmetGosterRecycler.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.hizmetGosterRecycler.adapter=hizmetAdapter

        binding.saatGosterRecycler.layoutManager= GridLayoutManager(requireContext(),3)
        binding.saatGosterRecycler.adapter= saatAdapter



        binding.musteriSecBtn.setOnClickListener {
            findNavController().popBackStack()
            navigate(R.id.musteriSecFragment)

        }

        //Güncel Tarih:
        cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd-MM-yyyy,E")
        val tarih = sdf.format(cal.time)
        binding.dateText.text = tarih
            randevuBilgileriOlustur()

        binding.nextDate.setOnClickListener {
            cal.add(Calendar.DAY_OF_YEAR,1)
            binding.dateText.text = sdf.format(cal.time)
            randevuBilgileriOlustur()
        }
        binding.backDate.setOnClickListener {

            cal.add(Calendar.DAY_OF_YEAR,-1)
            binding.dateText.text = sdf.format(cal.time)
            randevuBilgileriOlustur()
        }


       binding.randevuOlusturBtn.setOnClickListener {



           if (binding.musteriIsmiText.text.isEmpty()){
               AppUtil.longToast(requireContext(),"Müşteri seçiniz")
                return@setOnClickListener}else{
           if (personel==null){
               AppUtil.longToast(requireContext(),"Personel seçiniz")
                  return@setOnClickListener }
           if (hizmet==null){
               AppUtil.longToast(requireContext(),"Hizmet seçiniz")
                  return@setOnClickListener }
           if (secilenRandevuSaati==null){
               AppUtil.longToast(requireContext(),"Randevu saati seçiniz")
                  return@setOnClickListener }


            secilenRandevuSaati?.let {

                val randevuTime =   secilenRandevuSaati!!.saat.toTimestamp()

                val randevuMap = mapOf<String,Any>(
                    FIRMA_KODU to personel!!.firmaKodu,
                    PERSONEL_ID to personel!!.personelId,
                    MUSTERI_ID to musteri.musteriId,
                    HIZMET_ID to hizmet!!.hizmetId,
                    HIZMET_UCRETI to hizmet!!.fiyat,
                    RANDEVU_TIME to randevuTime,

                   )

                val randevu = Randevu(personel!!.firmaKodu,personel!!,musteri,hizmet!!, hizmet!!.fiyat,randevuTime)
                //FirebaseFirestore.getInstance().collection(RANDEVULAR).add(randevuMap)

              FirebaseFirestore.getInstance().collection(RANDEVULAR).document(AppUtil.randevuDocumentPath(randevu))
                   .set(randevuMap)
            }

            findNavController().popBackStack()
           AppUtil.longToast(requireContext(),"Yeni Randevu Eklendi..!")

           }
    }


       /* binding.dateText.setOnClickListener {

            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
            datePicker.addOnPositiveButtonClickListener {
                val date = Date(it)
          binding.dateText.setText(sdf.format(date))

            }
            datePicker.show(childFragmentManager, "tag")
        }*/


    }
//RANDEVU TARİH SAAT VB TÜM İŞLEMLERİ FONK DA YAP VE TARİH SEÇİMLERİNİN ALTINDA ÇAĞIR
    //BÖYLECE HER TARİH DEĞİŞİMİNDE BAŞTAN PERSONEL SEÇİMİNİ YENİLEMEYE GEREK KALAMAYACAK
    fun randevuBilgileriOlustur(){

        viewModel.secilenPersonel.observe(viewLifecycleOwner){ secilenPersonel->
            secilenPersonel?.let {

                personel = it


                val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
                val calismaGun = secilenPersonel.personelCalismaGun.find { it.gun==dayOfWeek }
                calismaGun?.let {
                    if (!it.calisiyormu){
                        AppUtil.longToast(context,"Personel seçilen tarihte çalışma saati dışındadır.")
                        binding.saatGosterRecycler.visibility=View.GONE
                        return@let

                    }else{
                        binding.saatGosterRecycler.visibility=View.VISIBLE

                        val baslangicSaatiCalendar = Calendar.getInstance()
                        baslangicSaatiCalendar.time = cal.time
                        baslangicSaatiCalendar.set(Calendar.HOUR_OF_DAY,it.baslangicSaat.saat)
                        baslangicSaatiCalendar.set(Calendar.MINUTE,it.baslangicSaat.dakika)

                        val bitisCal = Calendar.getInstance().apply {
                            time = cal.time
                            set(Calendar.HOUR_OF_DAY, it.bitisSaat.saat)
                            set(Calendar.MINUTE, it.bitisSaat.dakika)
                        }



                        val randevuSaatleri = mutableListOf<Date>()

                        val saatSdf = SimpleDateFormat("HH:mm")

                        val sdf = SimpleDateFormat("dd-MM-yyyy,E")

                        //cal.add(Calendar.MINUTE,personel.personelCalismaDakika)
                        val baslangicDate= sdf.format(baslangicSaatiCalendar.time)
                        val bitisDate = sdf.format(bitisCal.time)

                        println("while öncesi $baslangicDate, $bitisDate")



                        while ( baslangicSaatiCalendar.time<bitisCal.time) {
                            randevuSaatleri.add(baslangicSaatiCalendar.time)
                            baslangicSaatiCalendar.add(Calendar.MINUTE,personel!!.personelCalismaDakika)
                            println("WHİLE içi başlangic ttime : ${saatSdf.format(baslangicSaatiCalendar.time)} , bitisTime : ${saatSdf.format(bitisCal.time)} ")

                        }
                        /*val yeniRandevuSaatleri = mutableListOf<RandevuSaati>()
                        randevuSaatleri.forEach {
                            val dolumu = randevuSaatleri.contains(it)
                            yeniRandevuSaatleri.add(RandevuSaati(it,dolumu) )
                        }
                        saatAdapter.itemList = yeniRandevuSaatleri
                        saatAdapter.notifyDataSetChanged()*/

                        //Recycler içindeki personele verilebilecek randevu saatleri:
                        /*  val randevuSaatleriString = mutableListOf<String>()

                          randevuSaatleri.forEach {
                              randevuSaatleriString.add(saatSdf.format(it))
                          }*/

                        //Sorgu-> Seçili Tarih yani (1 gün) olacağını ayarla:

                        val sorguCalendar = Calendar.getInstance()
                        sorguCalendar.time = cal.time
                        sorguCalendar.set(Calendar.HOUR,0)
                        sorguCalendar.set(Calendar.MINUTE,0)
                        val sorguBaslangicTimestamp = Timestamp(sorguCalendar.time)
                        sorguCalendar.add(Calendar.DAY_OF_YEAR,1)
                        val sorguBitisTimestamp = Timestamp(sorguCalendar.time)


                        FirebaseFirestore.getInstance().collection(RANDEVULAR)
                            .whereEqualTo(FIRMA_KODU,personel!!.firmaKodu)
                            .whereEqualTo(PERSONEL_ID,personel!!.personelId)
                            //AŞAĞIDAKİ FİLTREYİ KALDIRINCA PERSONELİN TÜM RANDEVULARI GELİYOR, RANDEVU LİST DATE e
                            .whereGreaterThanOrEqualTo(RANDEVU_TIME,sorguBaslangicTimestamp)
                            .whereLessThanOrEqualTo(RANDEVU_TIME,sorguBitisTimestamp)
                            .get()
                            .addOnSuccessListener {
                                it?.let {
                                    val randevuListDate=it.toObjects(RandevuDTO::class.java).map { it.randevuTime.toDate()}


                                    val yeniRandevuSaatleri= mutableListOf<RandevuSaati>()

                                    randevuSaatleri.forEach {
                                        val dolu= randevuListDate.contains(it)
                                        yeniRandevuSaatleri.add(RandevuSaati(it,dolu))
                                    }
                                    saatAdapter.saatListesiniGuncelle(yeniRandevuSaatleri)
                                    //saatAdapter.itemList=yeniRandevuSaatleri
                                    saatAdapter.notifyDataSetChanged()
                                }
                            }
                            .addOnFailureListener {
                                it.printStackTrace()
                            }
                    }
                }
            }
        }

    }

            //FİREBASE DEN PERSONEL LİSTESİNİ ÇEK:
    fun getData(personeller : (List<Personel>)->Unit){

        FirebaseFirestore.getInstance().collection(PERSONELLER)
            //Firebase den sadece PERSONEL_RAND DUR u TRUE olanları getir
            .whereEqualTo(FIRMA_KODU,UserUtil.firmaKodu)
            .whereEqualTo(PERSONEL_RANDDUR,true)
            .get().addOnSuccessListener {
            it?.let {
                val personelListesi =  it.toObjects(Personel::class.java)
                personeller.invoke(personelListesi)
            }
            //println()
        }
            .addOnFailureListener {
                println(it)
            }
    }

    //FİREBASE DEN HİZMET LİSTESİNİ ÇEK:
    fun takeData(hizmetler : (List<Hizmet>)->Unit){

        FirebaseFirestore.getInstance().collection(HIZMETLER)
            .whereEqualTo(FIRMA_KODU,UserUtil.firmaKodu)
            .whereEqualTo(HIZMET_GOR_DUR,true)
            .get().addOnSuccessListener {
            it?.let {
                val hizmetList =  it.toObjects(Hizmet::class.java)
                hizmetler.invoke(hizmetList)
            }
            //println()
        }
            .addOnFailureListener {
                println(it)
          }
       }
    }