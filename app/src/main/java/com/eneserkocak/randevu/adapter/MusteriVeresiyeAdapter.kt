package com.eneserkocak.randevu.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.databinding.MusteriBorcRowBinding
import com.eneserkocak.randevu.model.*
import com.google.firebase.firestore.FirebaseFirestore

class MusteriVeresiyeAdapter:RecyclerView.Adapter<MusteriVeresiyeAdapter.musteriVeresiyeViewHolder>() {

    val randevuList= ArrayList<Randevu>()

    class musteriVeresiyeViewHolder(val binding:MusteriBorcRowBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): musteriVeresiyeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding= DataBindingUtil.inflate<MusteriBorcRowBinding>(inflater, R.layout.musteri_borc_row,parent,false)
        return musteriVeresiyeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: musteriVeresiyeViewHolder, position: Int) {
        val randevu = randevuList[position]
        holder.binding.musteriRandevu = randevu


        holder.binding.switchBtn.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked){
                holder.binding.mustBorcDetayLayout.visibility= View.VISIBLE
            }else{
                holder.binding.mustBorcDetayLayout.visibility= View.GONE
            }

        }



        holder.binding.tahsilatBtn.setOnClickListener {


            val alert = AlertDialog.Builder(holder.itemView.context)
            alert.setMessage("Müşteriye ait seçilen borç silinecek...                            Onaylıyormusunuz ?")

            alert.setPositiveButton("EVET", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {

                   val randevuGelirTuru= NAKIT
                    val veresiyeTutari=0

                    val randevuMap = mapOf<String,Any>(

                        RANDEVU_GELIR_TURU to randevuGelirTuru,
                        VERESIYE_TUTARI to veresiyeTutari
                    )

                    FirebaseFirestore.getInstance().collection(RANDEVULAR).document(AppUtil.randevuDocumentPath(randevu))
                        .update(randevuMap)
                        .addOnSuccessListener {

                            randevuList.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, randevuList.size)
                           notifyDataSetChanged()
                        }

                    AppUtil.longToast(it.context,"Tahsilat Yapıldı.")
                    dialog!!.dismiss()




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

    override fun getItemCount(): Int {
        return randevuList.size
    }

    fun musteriVeresiyeListesiniGuncelle(yeniMustVeresiyeListesi: List<Randevu>) {

        randevuList.clear()
        randevuList.addAll(yeniMustVeresiyeListesi)
        notifyDataSetChanged()


    }



}