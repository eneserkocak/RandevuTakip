package com.eneserkocak.randevu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.databinding.GunOzetiRowBinding
import com.eneserkocak.randevu.databinding.HizmetlisteRowBinding
import com.eneserkocak.randevu.model.Hizmet
import com.eneserkocak.randevu.model.Randevu
import com.eneserkocak.randevu.model.RandevuDTO


class GunOzetiAdapter:RecyclerView.Adapter<GunOzetiAdapter.GunOzetiViewHolder>() {

    val randevuList = arrayListOf<Randevu>()

    class GunOzetiViewHolder(val binding:GunOzetiRowBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GunOzetiViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<GunOzetiRowBinding>(inflater,R.layout.gun_ozeti_row ,parent,false)
        return GunOzetiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GunOzetiViewHolder, position: Int) {
            val randevu = randevuList[position]
            holder.binding.randevu=randevu

    }

    override fun getItemCount(): Int {
        return randevuList.size
    }

    fun randevuListesiniGuncelle(yeniRandevuListesi: List<Randevu>) {

        randevuList.clear()
        randevuList.addAll(yeniRandevuListesi)
        notifyDataSetChanged()
    }
}