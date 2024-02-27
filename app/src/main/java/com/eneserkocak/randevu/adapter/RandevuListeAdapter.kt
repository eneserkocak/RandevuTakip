package com.eneserkocak.randevu.adapter

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.Util.UserUtil
import com.eneserkocak.randevu.databinding.DialogRandevuIptalBinding
import com.eneserkocak.randevu.databinding.DialogRandevuTamamlaBinding

import com.eneserkocak.randevu.databinding.RecyclerRowRandevulistBinding
import com.eneserkocak.randevu.model.*
import com.eneserkocak.randevu.viewModel.AppViewModel
import com.google.firebase.firestore.FirebaseFirestore

class RandevuListeAdapter(val viewModel: AppViewModel,val secilenRandevu: (Randevu)->Unit):RecyclerView.Adapter<RandevuListeAdapter.RandevuListViewHolder>() {


    var mRandevuGeliri:Int=0
    var vRandevuGeliri:Int=0
    var  veresiyeTutari:Int=0
    val randevuListesi = arrayListOf<Randevu>()
    class RandevuListViewHolder(val binding:RecyclerRowRandevulistBinding):RecyclerView.ViewHolder(binding.root) {

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandevuListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<RecyclerRowRandevulistBinding>(inflater,R.layout.recycler_row_randevulist,parent,false)
        return RandevuListViewHolder(binding)

    }
    override fun onBindViewHolder(holder: RandevuListViewHolder, position: Int) {

        val randevu = randevuListesi[position]
        holder.binding.randevu = randevu
    //   val yeniRandevu= randevu.copy()



   //İPTAL ET, TAMAMLA VE NOTLAR İÇİN ALERT DİALOG OLUŞTUR...YENİ FRAGMENT A GEREK YOK

        holder.binding.rowIptalBtn.setOnClickListener {

            val binding = DialogRandevuIptalBinding.inflate(LayoutInflater.from(it.context))
            val dialog = AlertDialog.Builder(it.context).setView(binding.root).show()


            binding.dialogIptalBtn.setOnClickListener {


                val randevuDurumu= IPTAL_EDILDI

                val randevuMap = mapOf<String,Any>(

                    RANDEVU_DURUMU to randevuDurumu
                )
                FirebaseFirestore.getInstance().collection(RANDEVULAR).document(AppUtil.randevuDocumentPath(randevu))
                    .update(randevuMap)
                    .addOnSuccessListener {
                        randevu.randevuDurumu = IPTAL_EDILDI
                        notifyDataSetChanged()
                    }

                AppUtil.longToast(it.context,"Randevu İptal Edildi.")
                dialog.dismiss()

            }
            binding.vazgecBtn.setOnClickListener {
                dialog.dismiss()
         }

        }



        holder.binding.randTamamlaBtn.setOnClickListener {
            secilenRandevu.invoke(randevu)

            val binding = DialogRandevuTamamlaBinding.inflate(LayoutInflater.from(it.context))
            val dialog = AlertDialog.Builder(it.context).setView(binding.root).show()


            lateinit var mRandevuDurumu:String
            lateinit var mRandevuGelirTuru:String

            //NAKİT TAMAMLA
    binding.dialogTamamlaBtn.setOnClickListener {

            //eğer randevu geliri girmeden tamamlaya basarsak randGelir text string geliyo ınt e ceviremediği için çöküyor

                var gelir =binding.randGelir.text

                mRandevuGeliri= if (gelir!!.isEmpty()) randevu.hizmet.fiyat
                else binding.randGelir.text.toString().toInt()

                mRandevuDurumu="Randevu Tamamlandı"
                mRandevuGelirTuru= NAKIT



                val randevuMap = mapOf<String,Any>(
                    RANDEVU_GELIRI to mRandevuGeliri,
                    RANDEVU_DURUMU to mRandevuDurumu,
                    RANDEVU_GELIR_TURU to mRandevuGelirTuru

                )


                FirebaseFirestore.getInstance().collection(RANDEVULAR).document(AppUtil.randevuDocumentPath(randevu))
                    .update(randevuMap)
                    .addOnSuccessListener {

             //RANDEVU TAMAMLANINCA ..GELİR ÇIK GİR YAPMADAN GELMİYORDU.AŞAĞIDA VİEWMODEL I GÜNCELLE..SORUN ORTADAN KALKACAK
                     randevu.apply {
                                this.randevuGeliri=mRandevuGeliri
                                this.randevuDurumu=mRandevuDurumu
                                this.randevuGelirTuru=mRandevuGelirTuru
                            }
                        viewModel.randevuListesiniGuncelle()
                    }

                 AppUtil.longToast(it.context,"Randevu Tamamlandı.")
                dialog.dismiss()

            }

            //VERESİYE TAMAMLA

            lateinit var vRandevuDurumu:String
            lateinit var vRandevuGelirTuru:String

    binding.dialogVeresiyeBtn.setOnClickListener {

        val alert = AlertDialog.Builder(holder.itemView.context)
        alert.setMessage("Randevu ücreti, müşterininin hesabına geçilecek. Onaylıyormusunuz ?")

        alert.setPositiveButton("EVET", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {

                var gelir =binding.randGelir.text

                vRandevuGeliri= if (gelir!!.isEmpty()) randevu.hizmet.fiyat
                else binding.randGelir.text.toString().toInt()

                veresiyeTutari=vRandevuGeliri

                /* veresiyeTutari=  if (gelir!!.isEmpty()) randevu.hizmet.fiyat
                  else binding.randGelir.text.toString().toInt()*/

                vRandevuDurumu="Randevu Tamamlandı"
                vRandevuGelirTuru= VERESIYE

                val randevuMap = mapOf<String,Any>(
                    RANDEVU_GELIRI to vRandevuGeliri,
                    RANDEVU_DURUMU to vRandevuDurumu,
                    RANDEVU_GELIR_TURU to vRandevuGelirTuru,
                    VERESIYE_TUTARI to veresiyeTutari
                )

                FirebaseFirestore.getInstance().collection(RANDEVULAR).document(AppUtil.randevuDocumentPath(randevu))
                    .update(randevuMap)
                    .addOnSuccessListener {
                      randevu.apply {
                              this.randevuGeliri = vRandevuGeliri
                              this.randevuDurumu=vRandevuDurumu
                              this.randevuGelirTuru=vRandevuGelirTuru
                              this.veresiyeTutari=veresiyeTutari
                          }
                        viewModel.randevuListesiniGuncelle()

                    }

         //VERESİYE TAMAMLANIRSA MUSTERİ CLASS musteriVeresiye =TRUE YAP
                val mustDocument = UserUtil.firmaKodu.toString()+"-"+randevu.musteri.musteriId.toString()
                val musteriVeresiye= true

                val musteriMap= mapOf<String,Any>(

                    MUSTERI_VERESIYE to musteriVeresiye
                )

                FirebaseFirestore.getInstance().collection(MUSTERILER).document(mustDocument)
                    .update(musteriMap)


               AppUtil.longToast(it.context,"Randevu Tamamlandı.")
                dialog!!.dismiss()


            }
        })

        dialog!!.dismiss()


        alert.setNegativeButton("HAYIR", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                // findNavController().popBackStack()
            }
        })

        alert.show()


    }

            binding.vazgecBtn.setOnClickListener {
                dialog.dismiss()
            }





        }
        holder.binding.notBtn.setOnClickListener {
            secilenRandevu.invoke(randevu)
            findNavController(it).navigate(R.id.randevuNotlarFragment)
        }

        holder.binding.smsBtn.setOnClickListener {

            secilenRandevu.invoke(randevu)
            findNavController(it).navigate(R.id.smsGonderFragment)


        }

    }

    override fun getItemCount(): Int {
        return randevuListesi.size
    }

    fun randevuListesiniGuncelle(yeniRandevuListesi:List<Randevu>){

        randevuListesi.clear()
        randevuListesi.addAll(yeniRandevuListesi)
        notifyDataSetChanged()

    }




}