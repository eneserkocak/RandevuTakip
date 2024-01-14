package com.eneserkocak.randevu.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.databinding.DialogRandevuIptalBinding
import com.eneserkocak.randevu.databinding.DialogRandevuTamamlaBinding
import com.eneserkocak.randevu.databinding.DialogSmsGonderBinding

import com.eneserkocak.randevu.databinding.RecyclerRowRandevulistBinding
import com.eneserkocak.randevu.model.*
import com.eneserkocak.randevu.viewModel.AppViewModel
import com.eneserkocak.randevu.view_ayarlar.gun_saat_ayar.GunSaatPersListeFragmentDirections
import com.eneserkocak.randevu.view_randevuListesi.RandevuListesiFragmentDirections
import com.google.firebase.firestore.FirebaseFirestore

class RandevuListeAdapter(val secilenRandevu: (Randevu)->Unit):RecyclerView.Adapter<RandevuListeAdapter.RandevuListViewHolder>() {

    var randevuGeliri:Int=0
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


           // var randevuGeliri:Int=0
            lateinit var randevuDurumu:String

    binding.dialogTamamlaBtn.setOnClickListener {

//eğer randevu geliri girmeden tamamlaya basarsak randGelir text string geliyo ınt e ceviremediği için çöküyor

                var gelir =binding.randGelir.text

                val personelAdi= randevu.personel
                val randevuTime= randevu.randevuTime


                randevuGeliri= if (gelir.isEmpty()) randevu.hizmet.fiyat
                else binding.randGelir.text.toString().toInt()

                randevuDurumu="Randevu Tamamlandı"

                /*val sharedPreferences = AppUtil.getSharedPreferences(requireContext())
                sharedPreferences.edit().putString(RANDEVU_GELIR,randGeliri).apply()*/

                println("YAZDIR: ${personelAdi}")
                println("YAZDIR: ${randevuTime}")



                val randevuMap = mapOf<String,Any>(
                    RANDEVU_GELIRI to randevuGeliri,
                    RANDEVU_DURUMU to randevuDurumu
                )


                FirebaseFirestore.getInstance().collection(RANDEVULAR).document(AppUtil.randevuDocumentPath(randevu))
                    .update(randevuMap)
                    .addOnSuccessListener {
                        randevu.randevuDurumu = TAMAMLANDI
                        notifyDataSetChanged()
                    }

                 AppUtil.longToast(it.context,"Randevu Tamamlandı.")
                dialog.dismiss()
            }
            binding.vazgecBtn.setOnClickListener {
                dialog.dismiss()
            }

           //findNavController(it).navigate(R.id.randevuTamamlaFragment)



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