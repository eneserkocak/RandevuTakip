package com.eneserkocak.randevu.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.databinding.FragmentGunOzetiGiderBinding
import com.eneserkocak.randevu.databinding.GunOzetiGiderRowBinding
import com.eneserkocak.randevu.databinding.GunOzetiRowBinding
import com.eneserkocak.randevu.model.GIDERLER
import com.eneserkocak.randevu.model.Gider
import com.eneserkocak.randevu.model.RANDEVULAR
import com.eneserkocak.randevu.model.Randevu
import com.google.firebase.firestore.FirebaseFirestore

class GunOzetiGiderAdapter:RecyclerView.Adapter<GunOzetiGiderAdapter.GunOzetiGiderViewHolder>() {

    val giderList = arrayListOf<Gider>()

    class GunOzetiGiderViewHolder(val binding: GunOzetiGiderRowBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GunOzetiGiderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<GunOzetiGiderRowBinding>(inflater,R.layout.gun_ozeti_gider_row ,parent,false)
        return GunOzetiGiderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GunOzetiGiderViewHolder, position: Int) {
        val gider=giderList[position]
        holder.binding.gider=gider


        holder.binding.giderSilBtn.setOnClickListener {
              // AppUtil.giderSil(gider,{})
            giderSilDialog(holder.itemView.context,position)
        }

    }

    override fun getItemCount(): Int {
        return giderList.size
    }

    fun giderListesiniGuncelle(yeniGiderListesi: List<Gider>) {

        giderList.clear()
        giderList.addAll(yeniGiderListesi)
        notifyDataSetChanged()
    }

    fun giderSilDialog(context: Context, position: Int){

        val alert = AlertDialog.Builder(context)
        alert.setMessage("Gider kaydı silinsin mi?")

        alert.setPositiveButton("EVET", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                //Firebase den gider kaydı silme işlemi yap

                FirebaseFirestore.getInstance().collection(GIDERLER).document(AppUtil.giderSil(giderList.get(position),{})
                    .toString())
                    .delete()
                    .addOnSuccessListener {
                        giderList.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, giderList.size)
                        AppUtil.longToast(context,"Gider kaydı silindi..")
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