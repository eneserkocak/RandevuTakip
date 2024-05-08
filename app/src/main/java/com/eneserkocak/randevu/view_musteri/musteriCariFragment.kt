package com.eneserkocak.randevu.view_musteri

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isEmpty
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.Util.UserUtil
import com.eneserkocak.randevu.adapter.MusteriVeresiyeAdapter
import com.eneserkocak.randevu.adapter.PersonelRandevuAdapter
import com.eneserkocak.randevu.databinding.DialogRandevuTamamlaBinding
import com.eneserkocak.randevu.databinding.DialogSmsBorcBinding
import com.eneserkocak.randevu.databinding.FragmentMusteriCariBinding
import com.eneserkocak.randevu.model.*
import com.eneserkocak.randevu.view.BaseFragment
import com.google.firebase.firestore.FirebaseFirestore


class musteriCariFragment : BaseFragment<FragmentMusteriCariBinding>(R.layout.fragment_musteri_cari) {

    lateinit var secilenMusteri: Musteri
    var filtreMustBorcListesi= listOf<Randevu>()
    lateinit var borcTutari:String

    lateinit var adapter: MusteriVeresiyeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter= MusteriVeresiyeAdapter(viewModel)

        binding.musteriVeresiyeRecycler.layoutManager= LinearLayoutManager(requireContext())
        binding.musteriVeresiyeRecycler.adapter= adapter

     //   checkPermission()

        viewModel.veresiyeRandVerileriGetir()
        viewModel.veresiyeTamamlananRandevuListesi.observe(viewLifecycleOwner){

            it?.let {

                viewModel.secilenMusteri.observe(viewLifecycleOwner) { secilenMusteri ->
                    secilenMusteri?.let { musteri ->


                        binding.musteriAdiTxt.text=musteri.musteriAdi
                        filtreMustBorcListesi = it.filter {
                            it.musteri.musteriAdi == secilenMusteri.musteriAdi
                        }

                        println("CARİİİ: ${filtreMustBorcListesi.size}")


                        if (filtreMustBorcListesi.isEmpty()) {
                            binding.toplamBorcCount.setText("0")
                            binding.musteriVeresiyeRecycler.visibility = View.GONE
                            binding.bilgiText.setText("Müşteriye ait borç bilgisi bulunmamaktadır..!")
                            binding.bilgiText.setTextColor(Color.parseColor("#C53D3D"))
                            binding.bilgiText.visibility = View.VISIBLE
                            borcTutari="0"

                        } else {

                            var borc = 0
                            filtreMustBorcListesi.forEach {
                                borc = borc + it.veresiyeTutari
                                binding.toplamBorcCount.text = borc.toString()

        // borcTutarı burda değer ver..Aşağıda DİALOG içinde müşteriye borç tutarı nı burdan GÜNCEL ALACAĞIZ.DEĞER DEĞİŞİNCE
                //MÜŞTERİ BORCU SMS DE CANLI GÜNCELLENMİŞ OLACAK


                                    borcTutari = borc.toString()

                            }


                            binding.musteriVeresiyeRecycler.visibility = View.VISIBLE
                            binding.bilgiText.visibility = View.GONE

                            val list=filtreMustBorcListesi.sortedBy {
                                it.randevuTime
                            }

                            adapter.musteriVeresiyeListesiniGuncelle(list)

                    //MUSTERİ SINIFINA BORÇ TUTARINI EKLE -> FİREBASE  -> borç sms için burda kaydettiğimiz toplam borcu kullan
                            val mustDocument = UserUtil.firmaKodu.toString()+"-"+musteri.musteriId.toString()
                            val musteriBorc= borc


                            val musteriMap= mapOf<String,Any>(
                                MUSTERI_BORC to musteriBorc
                            )

                            FirebaseFirestore.getInstance().collection(MUSTERILER).document(mustDocument)
                                .update(musteriMap)
                        }



                        //MUSTERİ VERESİYE RECYCLER LİST BOŞ İSE FİREBASE DE-> MÜSTERİ MODEL-> musteriVeresiye -> FALSE YAP


                        if (binding.toplamBorcCount.text =="0") {
                            //Müşteri Sınıfında Borcu Var -> False YAP

                            binding.musteriVeresiyeRecycler.visibility=View.GONE
                            val mustDocument = UserUtil.firmaKodu.toString()+"-"+musteri.musteriId.toString()
                            val musteriVeresiye= false

                            val musteriMap= mapOf<String,Any>(
                                  MUSTERI_VERESIYE to musteriVeresiye


                            )

                            FirebaseFirestore.getInstance().collection(MUSTERILER).document(mustDocument)
                                .update(musteriMap)

                        }else{
                            binding.musteriVeresiyeRecycler.visibility=View.VISIBLE

                            val mustDocument = UserUtil.firmaKodu.toString()+"-"+musteri.musteriId.toString()
                            val musteriVeresiye= true

                            val musteriMap= mapOf<String,Any>(
                                MUSTERI_VERESIYE to musteriVeresiye
                            )

                            FirebaseFirestore.getInstance().collection(MUSTERILER).document(mustDocument)
                                .update(musteriMap)

                        }




            //SMS DİALOG AŞAĞIDA

        binding.msjButton.setOnClickListener {
             //  findNavController().navigate(R.id.smsBorcFragment)

            val binding = DialogSmsBorcBinding.inflate(LayoutInflater.from(it.context))
            val dialog = AlertDialog.Builder(it.context).setView(binding.root).show()


            binding.whatsappSmsBtn.setOnClickListener {



                val musteriAdi=musteri.musteriAdi.uppercase()
                val musteriTel= "+90${musteri.musteriTel}"

                val firmaAdi= UserUtil.firmaIsim.uppercase()

                val musteriBorc= borcTutari

                if(musteriTel.length==14){

                    val url = "https://api.whatsapp.com/send?phone=$musteriTel&text=Sayın $musteriAdi, hesabınıza ait, $musteriBorc TL borç bakiyeniz bulunmaktadır... $firmaAdi"
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    context?.startActivity(i)

                   dialog.dismiss()

                    Toast.makeText(requireContext(),"Müşteriye whatsapp bilgi mesajı gönderiliyor..!",Toast.LENGTH_SHORT).show()
                   // AppUtil.longToast(requireContext(),"Müşteriye whatsapp bilgi mesajı gönderiliyor..!")

                }else{
                    AppUtil.longToast(requireContext(),"Müşteriye ait telefon numarası kayıtlı değil veya hatalı kaydedilmiş.")
                }

            }

            //TELEFON SMS GÖNDER

            binding.telefonSmsBtn.setOnClickListener {

                val musteriBorc= borcTutari

                val mustAdi=musteri.musteriAdi.uppercase()
                val mustTel= "${musteri.musteriTel}"

                val firmaAdi= UserUtil.firmaIsim.uppercase()

                val smsMesaj= "Sayın $mustAdi, hesabınıza ait, $musteriBorc TL borç bakiyeniz bulunmaktadır... $firmaAdi"

                if(mustTel.length==11){
                    val smsIntent= Intent(Intent.ACTION_VIEW)

                    // smsIntent.setType("vnd.android-dir/mms-sms")
                    smsIntent.data = Uri.parse("sms:$mustTel")
                    smsIntent.putExtra("sms_body", "$smsMesaj")
                    startActivity(smsIntent)

                  dialog.dismiss()

                    Toast.makeText(requireContext(),"Müşteriye sms bilgi mesajı gönderiliyor..!",Toast.LENGTH_SHORT).show()
                   // AppUtil.longToast(requireContext(),"Müşteriye sms bilgi mesajı gönderiliyor..!")
                }else{
                    AppUtil.longToast(requireContext(),"Müşteriye ait telefon numarası kayıtlı değil veya hatalı kaydedilmiş.")
                }
     //Sayın $musteriAdi, $randevuTarih günü, saat: $randevuSaat 'da randevu kaydınız oluşturulmuştur.Bizi tercih ettiğiniz için teşekkür eder, İyi günler dileriz.
            }

            binding.vazgecBtn.setOnClickListener {
                dialog.dismiss()
            }


        }

                    }

                }

            }}
    }
    private fun checkPermission(){
        if (ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.SEND_SMS),101)

        }
    }
}