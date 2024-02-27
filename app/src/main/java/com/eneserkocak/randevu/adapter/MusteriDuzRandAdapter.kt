package com.eneserkocak.randevu.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.icu.text.Transliterator.Position
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.Util.PictureUtil
import com.eneserkocak.randevu.databinding.MusteriDuzRandRowBinding
import com.eneserkocak.randevu.model.RANDEVULAR
import com.eneserkocak.randevu.model.Randevu
import com.google.firebase.firestore.FirebaseFirestore

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MusteriDuzRandAdapter: RecyclerView.Adapter<MusteriDuzRandAdapter.MusteriRandevuViewHolder>() {

    val randevuList= ArrayList<Randevu>()
    //randevuyu burda tanımlayınca Silerken hep aynı position geliyor ve tek bir row siliniyor..Diğerleri çık gir yapmadan SİLİNMİYOR
    // lateinit var randevu:Randevu

    class MusteriRandevuViewHolder(val binding: MusteriDuzRandRowBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusteriRandevuViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding= DataBindingUtil.inflate<MusteriDuzRandRowBinding>(inflater, R.layout.musteri_duz_rand_row,parent,false)
        return MusteriRandevuViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MusteriRandevuViewHolder, position: Int) {


       val randevu = randevuList[position]
         holder.binding.musteriRandevu = randevu


        holder.binding.switchBtn.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked){
                holder.binding.musteriDetayiLayout.visibility= View.VISIBLE
            }else{
                holder.binding.musteriDetayiLayout.visibility= View.GONE
            }

        }


       holder.binding.deleteButton.setOnClickListener {

            musteriRandevuSilDialog(holder.itemView.context,position)

       }
    }

    override fun getItemCount(): Int {
        return randevuList.size
    }



    fun musteriRandevuListesiniGuncelle(yeniMusteriRandListesi: List<Randevu>) {

        randevuList.clear()
        randevuList.addAll(yeniMusteriRandListesi)
        notifyDataSetChanged()


    }
    fun musteriRandevuSilDialog(context: Context,position: Int){

        val alert = AlertDialog.Builder(context)
        alert.setMessage("Randevu silinsin mi?")

        alert.setPositiveButton("EVET", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                //Firebase den müşterinin Randevusunu silme işlemi yap

                FirebaseFirestore.getInstance().collection(RANDEVULAR).document(AppUtil.randevuDocumentPath(randevuList.get(position)))
                    .delete()
                    .addOnSuccessListener {
                        randevuList.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, randevuList.size)
                        AppUtil.longToast(context,"Randevu silindi..")


                    }.addOnFailureListener {
                        it.printStackTrace()
                    }
            }
        })
        alert.setNegativeButton("HAYIR", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {

            }
        })
        alert.show()
    }
}










