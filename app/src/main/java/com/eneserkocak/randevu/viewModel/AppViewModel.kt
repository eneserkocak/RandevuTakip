package com.eneserkocak.randevu.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.db_firmaMaps.FirmaMapsDatabase
import com.eneserkocak.randevu.model.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AppViewModel(app:Application):AndroidViewModel(app) {


       val randevuListesi = MutableLiveData<List<Randevu>>()
      val raporRandevuListesi = MutableLiveData<List<Randevu>>()
       val tamamlananRandevuListesi = MutableLiveData<List<Randevu>>()


       val kaydedilenFirma = MutableLiveData<Firma>()
       var secilenPersonel = MutableLiveData<Personel>()
       var secilenHizmet = MutableLiveData<Hizmet>()
       var secilenMusteri = MutableLiveData<Musteri>()
       var secilenRandevu = MutableLiveData<Randevu>()
       val secilenKonum = MutableLiveData<Konum>()
       val secilenGider=MutableLiveData<Gider>()






    //RANDEVU LİSTESİ FRAGMENT TA TAMAMLANAN RANDEVULAR RANDEVU DTO DAN->RANDEVU MODELİNE ÇEVİR VE FİREBASE DEN ÇEK:
    //TÜM RANDEVULARI ÇEKECEĞİZ

        fun randVerileriGetir(){
       //  indirilenRandevuListesi ni burda tanımla ..Aaşağıda Kullanacağız.
      //  kULLANMASAK FOR içinde old. için indirilenRandevulara sürekli (add) ekleme yapar...

       val indirilenRandevuListesi = mutableListOf<Randevu>()

        val queryRef= FirebaseFirestore.getInstance().collection(RANDEVULAR).get()
        viewModelScope.launch(Dispatchers.IO ) {

        var indirilenRandevular = queryRef.await().toObjects(RandevuDTO::class.java)

       indirilenRandevular.forEach { randevu->

       viewModelScope.launch(Dispatchers.IO) {

           val musteri = async {
                    //   println(" MUSTERİ THREAD :${Thread.currentThread().name}")
               FirebaseFirestore.getInstance().collection("musteriler")
               .document("${randevu.firmaId}-${randevu.musteriId}")
                      .get().await().toObject(Musteri::class.java)
                                }.await()
           val personel = async {
               //   println(" MUSTERİ THREAD :${Thread.currentThread().name}")
               FirebaseFirestore.getInstance().collection("personeller")
                   .document("${randevu.firmaId}-${randevu.personelId}")
                   .get().await().toObject(Personel::class.java)
                                }.await()
           val hizmet = async {
               //   println(" MUSTERİ THREAD :${Thread.currentThread().name}")
               FirebaseFirestore.getInstance().collection("hizmetler")
                   .document("${randevu.firmaId}-${randevu.hizmetId}")
                   .get().await().toObject(Hizmet::class.java)
                               }.await()

        //COURİTİNE İÇİNDE İLGİLİ SINIFLARDAN (HİZMET, MUSTRİ VB) ÇEKTİĞİMİZ VERİLERİ AŞAĞIDA RANDEVU MODEL İNE ÇEVİRİYORUZ.
       /*val indirilenRandevu=Randevu(
           randevu.firmaId,
           personel?.personelAdi!!,
           musteri?.musteriAdi!!,
           hizmet?.hizmetAdi!!,
           hizmet?.fiyat!!,
           randevu.randevuTime
           )*/
       //  DİKKATT!!  AŞAĞIDA MODELE GÖRE SIRASIYLA GİİRİLMELİ--YOKSA MODELDEN ÇAĞIRINCA BİRBİRİNİN YERİNE GELİYOR
           val indirilenRandevu=Randevu(
               randevu.firmaId,
               personel!!,
               musteri!!,
               hizmet!!,
               randevu.randevuGeliri,
               randevu.randevuTime,
               randevu.randevuDurumu,
               randevu.randevuNotu,
              // randevu.randevuTarih,
              // randevu.randevuSaat,

           )
           println("İNDİRİLEN RANDEVU : $indirilenRandevu")

               indirilenRandevuListesi.add(indirilenRandevu)

           //indirilenRandevuListesi(Boş liste) ile başla .indirilen randevular ı ekleyince (EŞİTLENİNCE) DURR.
           if (indirilenRandevuListesi.size==indirilenRandevular.size){
               withContext(Dispatchers.Main) {
                   randevuListesi.value = indirilenRandevuListesi
             }
           }
         }
       }
     }
   }

    //GÜN ÖZETİ FRAGMENT TA TAMAMLANAN RANDEVULAR RANDEVU DTO DAN->RANDEVU MODELİNE ÇEVİR VE FİREBASE DEN ÇEK:
        //SADECE TAMAMLANAN RANDEVULARI ÇEKECEĞİZ
    fun tamamlananRandVerileriGetir(){

        //  cekilenRandevuListesi ni burda tanımla ..Aaşağıda Kullanacağız.
        //  kULLANMASAK FOR içinde old. için indirilenRandevulara sürekli (add) ekleme yapar...

        val cekilenRandevuListesi = mutableListOf<Randevu>()

        val queryRef= FirebaseFirestore.getInstance().collection(RANDEVULAR)
            .whereEqualTo(RANDEVU_DURUMU,"Randevu Tamamlandı")
            .get()
        viewModelScope.launch(Dispatchers.IO ) {

            var cekilenRandevular = queryRef.await().toObjects(RandevuDTO::class.java)

           cekilenRandevular.forEach { randevu->

                viewModelScope.launch(Dispatchers.IO) {

                    val musteri = async {
                        //   println(" MUSTERİ THREAD :${Thread.currentThread().name}")
                        FirebaseFirestore.getInstance().collection("musteriler")
                            .document("${randevu.firmaId}-${randevu.musteriId}")
                            .get().await().toObject(Musteri::class.java)
                    }.await()
                    val personel = async {
                        //   println(" MUSTERİ THREAD :${Thread.currentThread().name}")
                        FirebaseFirestore.getInstance().collection("personeller")
                            .document("${randevu.firmaId}-${randevu.personelId}")
                            .get().await().toObject(Personel::class.java)
                    }.await()
                    val hizmet = async {
                        //   println(" MUSTERİ THREAD :${Thread.currentThread().name}")
                        FirebaseFirestore.getInstance().collection("hizmetler")
                            .document("${randevu.firmaId}-${randevu.hizmetId}")
                            .get().await().toObject(Hizmet::class.java)
                    }.await()

  //COURİTİNE İÇİNDE İLGİLİ SINIFLARDAN (HİZMET, MUSTERİ VB) ÇEKTİĞİMİZ VERİLERİ AŞAĞIDA RANDEVU MODEL İNE ÇEVİRİYORUZ.
//  DİKKATT!!  AŞAĞIDA MODELE GÖRE SIRASIYLA GİİRİLMELİ--YOKSA MODELDEN ÇAĞIRINCA BİRBİRİNİN YERİNE GELİYOR
                    val indirilenRandevu=Randevu(
                        randevu.firmaId,
                        personel!!,
                        musteri!!,
                        hizmet!!,
                        randevu.randevuGeliri,
                        randevu.randevuTime,
                        randevu.randevuDurumu,
                        randevu.randevuNotu,
                      //  randevu.randevuTarih,
                      //  randevu.randevuSaat,
                    )


                    println("İNDİRİLEN RANDEVU : $indirilenRandevu")

                    cekilenRandevuListesi.add(indirilenRandevu)

                    //indirilenRandevuListesi(Boş liste) ile başla .indirilen randevular ı ekleyince (EŞİTLENİNCE) DURR.
                    if (cekilenRandevuListesi.size==cekilenRandevular.size){
                        withContext(Dispatchers.Main) {
                            tamamlananRandevuListesi.value = cekilenRandevuListesi
                        }
                    }


                }
            }
        }
    }

    //RAPORLARDA kullanacağız-> SEÇİLEN TARİH ARALIĞINI AL..AŞAĞIDA . whereGrater- .whereLess
    //TAMAMLANAN VE SEÇİLİ TARİH ARALIĞINDAKİ RANDEVULARI ÇEKECEĞİZ
    //fun raporRandVerileriGetir(firmaId : Int,personelId:Int){
    fun raporRandVerileriGetir(tarih1:Timestamp,tarih2:Timestamp){

        //  indirilenRandevuListesi ni burda tanımla ..Aaşağıda Kullanacağız.
        //  kULLANMASAK FOR içinde old. için indirilenRandevulara sürekli (add) ekleme yapar...


        val indirilenRandevuListesi = mutableListOf<Randevu>()

            val queryRef= FirebaseFirestore.getInstance().collection(RANDEVULAR)
           //val queryRef =  AppUtil.randevuQuery()
            .whereEqualTo(RANDEVU_DURUMU,"Randevu Tamamlandı")
            //.whereEqualTo(PERSONEL_ID,personelId)
                .whereGreaterThanOrEqualTo(RANDEVU_TIME,tarih1)
                .whereLessThanOrEqualTo(RANDEVU_TIME,tarih2)
            .get()
        viewModelScope.launch(Dispatchers.IO ) {

            var indirilenRandevular = queryRef.await().toObjects(RandevuDTO::class.java)

    //BURADA BOŞ MU KONTROLÜ YAP..YOKSA AŞAĞIDA FOR DÖNGÜSÜNDE BOŞ SA DÖNGÜYE GİRMİYOR
    //VE RAPORLARDA BOŞ TARİH SEÇİNCE VERİ BURDAN RAPORLARA GELMİYOR VE RAKAMLAR "0" GÖRÜNMÜYOR..
            if (indirilenRandevular.isEmpty()) {
                withContext(Dispatchers.Main) {
                    raporRandevuListesi.value = emptyList()
                    return@withContext
                }
            }

            indirilenRandevular.forEach { randevu->

                viewModelScope.launch(Dispatchers.IO) {

                    val musteri = async {
                        //   println(" MUSTERİ THREAD :${Thread.currentThread().name}")
                        FirebaseFirestore.getInstance().collection("musteriler")
                            .document("${randevu.firmaId}-${randevu.musteriId}")
                            .get().await().toObject(Musteri::class.java)
                    }.await()
                    val personel = async {
                        //   println(" MUSTERİ THREAD :${Thread.currentThread().name}")
                        FirebaseFirestore.getInstance().collection("personeller")
                            .document("${randevu.firmaId}-${randevu.personelId}")
                            .get().await().toObject(Personel::class.java)
                    }.await()
                    val hizmet = async {
                        //   println(" MUSTERİ THREAD :${Thread.currentThread().name}")
                        FirebaseFirestore.getInstance().collection("hizmetler")
                            .document("${randevu.firmaId}-${randevu.hizmetId}")
                            .get().await().toObject(Hizmet::class.java)
                    }.await()

                //COURİTİNE İÇİNDE İLGİLİ SINIFLARDAN (HİZMET, MUSTRİ VB) ÇEKTİĞİMİZ VERİLERİ AŞAĞIDA RANDEVU MODEL İNE ÇEVİRİYORUZ.
                    // (STRİNG DEĞERLERİ KULLANMAK İÇİN)

   //  DİKKATT!!  AŞAĞIDA MODELE GÖRE SIRASIYLA GİİRİLMELİ--YOKSA MODELDEN ÇAĞIRINCA BİRBİRİNİN YERİNE GELİYOR
                    val indirilenRandevu=Randevu(
                        randevu.firmaId,
                        personel!!,
                        musteri!!,
                        hizmet!!,
                        randevu.randevuGeliri,
                        randevu.randevuTime,
                        randevu.randevuDurumu,
                        randevu.randevuNotu,
                   //     randevu.randevuTarih,
                   //     randevu.randevuSaat,
                        )
                    println("İNDİRİLEN RANDEVU : $indirilenRandevu")

                    indirilenRandevuListesi.add(indirilenRandevu)

                    //indirilenRandevuListesi(Boş liste) ile başla .indirilen randevular ı ekleyince (EŞİTLENİNCE) DURR.
                    if (indirilenRandevuListesi.size==indirilenRandevular.size){
                        withContext(Dispatchers.Main) {
                            raporRandevuListesi.value = indirilenRandevuListesi
                        }
                    }
                }
            }
        }
    }




}
    //FİREBASE DEN ÖNCE ROOM işlemlerini VİEWMODEL DAN ÇEKEBİLİYORDUK:

//val dao= PersonelDatabase.getInstance(app)!!.personelDao()
//val personelList= dao.getAll()
/*fun personelGuncelle(personel: Personel){
             dao.update(personel)
}
fun personelSil(personel: Personel){
             dao.delete(personel)
}*/






