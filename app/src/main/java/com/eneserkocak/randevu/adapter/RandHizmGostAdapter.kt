package com.eneserkocak.randevu.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.databinding.RandevualHizmetRowBinding
import com.eneserkocak.randevu.model.ClickedButton

import com.eneserkocak.randevu.model.Hizmet
import com.eneserkocak.randevu.model.Personel

class RandHizmGostAdapter(val secilenHizmet : (Hizmet)->Unit):RecyclerView.Adapter<RandHizmGostAdapter.HizmGosterViewHolder>() {

    val hizmetListesi = arrayListOf<Hizmet>()
    private var selectedItem: Int?= null

    class HizmGosterViewHolder(val binding: RandevualHizmetRowBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HizmGosterViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        val binding= DataBindingUtil.inflate<RandevualHizmetRowBinding>(inflater, R.layout.randevual_hizmet_row,parent,false)
        return HizmGosterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HizmGosterViewHolder, position: Int) {
            val hizmet= hizmetListesi[position]
            holder.binding.hizmet= hizmet

        holder.itemView.setOnClickListener {
            secilenHizmet.invoke(hizmet)

            selectedItem=position
            notifyDataSetChanged()
        }
        if(selectedItem == position) {
            // holder.itemView.setBackgroundColor(Color.parseColor("#EBEBEB"))
            holder.binding.clickedHizmet.setBackgroundColor(Color.parseColor("#EBEBEB"))
        }else {
            // holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
            holder.binding.clickedHizmet.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }

    }

    override fun getItemCount(): Int {
        return hizmetListesi.size
    }

    fun hizmetListGuncelle(yeniHizmetListesi: List<Hizmet>) {
        hizmetListesi.clear()
        hizmetListesi.addAll(yeniHizmetListesi)
        notifyDataSetChanged()
    }
}