package com.eneserkocak.randevu.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.databinding.MusteriDuzRandRowBinding
import com.eneserkocak.randevu.databinding.PersonelRandRowBinding
import com.eneserkocak.randevu.model.RANDEVULAR
import com.eneserkocak.randevu.model.Randevu
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PersonelRandevuAdapter:RecyclerView.Adapter<PersonelRandevuAdapter.RandevuViewHolder>() {

    val randevuList= ArrayList<Randevu>()

    class RandevuViewHolder(val binding:PersonelRandRowBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandevuViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding= DataBindingUtil.inflate<PersonelRandRowBinding>(inflater, R.layout.personel_rand_row,parent,false)
        return RandevuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RandevuViewHolder, position: Int) {

       val randevu = randevuList[position]
        holder.binding.personelRandevu = randevu




        holder.binding.switchBtn.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked){
                holder.binding.personelDetayiLayout.visibility= View.VISIBLE
            }else{
                holder.binding.personelDetayiLayout.visibility= View.GONE
            }

        }




        holder.binding.deleteButton.setOnClickListener {

           personelRandevuSilDialog(holder.itemView.context,position)

        }

    }

    override fun getItemCount(): Int {
        return randevuList.size
    }

    fun personelRandevuListesiniGuncelle(yeniPersonelRandListesi: List<Randevu>) {

        randevuList.clear()
        randevuList.addAll(yeniPersonelRandListesi)
        notifyDataSetChanged()


    }
    fun personelRandevuSilDialog(context: Context, position: Int){

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
                    }
                    .addOnFailureListener {
                        it.printStackTrace()

                    }
            }
        })
        alert.setNegativeButton("HAYIR", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                //findNavController().popBackStack()
            }
        })
        alert.show()
    }



}