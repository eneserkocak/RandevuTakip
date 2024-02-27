package com.eneserkocak.randevu.adapter

import android.icu.text.Transliterator.Position
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.PictureUtil
import com.eneserkocak.randevu.databinding.GunOzetiRowBinding

import com.eneserkocak.randevu.databinding.RaporlarRowBinding
import com.eneserkocak.randevu.model.Personel
import com.eneserkocak.randevu.model.PersonelRandevu
import com.eneserkocak.randevu.model.Randevu

class RaporlarAdapter:RecyclerView.Adapter<RaporlarAdapter.RaporlarViewHolder>() {

           val persRandevuList = arrayListOf<PersonelRandevu>()

    class RaporlarViewHolder(val binding: RaporlarRowBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RaporlarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<RaporlarRowBinding>(inflater,R.layout.raporlar_row ,parent,false)
        return RaporlarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RaporlarViewHolder, position: Int) {

        val randevu = persRandevuList[position]
        holder.binding.randevu=randevu

    }

    override fun getItemCount(): Int {
       return persRandevuList.size
    }

    fun persRandListesiniGuncelle(yeniRandevuListesi: List<PersonelRandevu>) {

        persRandevuList.clear()
        persRandevuList.addAll(yeniRandevuListesi)
        notifyDataSetChanged()

    }


}