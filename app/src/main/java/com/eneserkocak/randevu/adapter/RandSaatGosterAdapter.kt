package com.eneserkocak.randevu.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.databinding.RandevualSaatRowBinding
import com.eneserkocak.randevu.model.Gider
import com.eneserkocak.randevu.model.Hizmet
import com.eneserkocak.randevu.model.RandevuSaati
import java.text.SimpleDateFormat


class RandSaatGosterAdapter(val secilenRandevuSaati: (RandevuSaati)->Unit): RecyclerView.Adapter<RandSaatGosterAdapter.RandSaatGostViewHolder>() {

    val saatList= arrayListOf<RandevuSaati>()
    var selectedItem: Int?= null

    class RandSaatGostViewHolder(val binding: RandevualSaatRowBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandSaatGostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding= DataBindingUtil.inflate<RandevualSaatRowBinding>(inflater, R.layout.randevual_saat_row,parent,false)
        return RandSaatGostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RandSaatGostViewHolder, position: Int) {

        val saat= saatList[position]
        holder.binding.saat = saat

        val sdf = SimpleDateFormat("HH:mm")
        holder.binding.saatTextV.text = sdf.format(saat.saat)

        holder.itemView.setOnClickListener {
        secilenRandevuSaati.invoke(saat)

            selectedItem=position
            notifyDataSetChanged()
        }

        if(selectedItem == position) {
            // holder.itemView.setBackgroundColor(Color.parseColor("#EBEBEB"))
            holder.binding.clickedSaat.setBackgroundColor(Color.parseColor("#EBEBEB"))
        }else {
            // holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
            holder.binding.clickedSaat.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }


    }

    override fun getItemCount(): Int {
        return saatList.size
    }
    fun saatListesiniGuncelle(yeniSaatListesi: List<RandevuSaati>) {

        saatList.clear()
        saatList.addAll(yeniSaatListesi)
        notifyDataSetChanged()
    }
}
