package com.eneserkocak.randevu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.PictureUtil
import com.eneserkocak.randevu.databinding.MusteriBorcRowBinding
import com.eneserkocak.randevu.databinding.MusteriBorclularListRowBinding
import com.eneserkocak.randevu.model.MusteriVeresiye

class BorclularAdapter: RecyclerView.Adapter<BorclularAdapter.BorclularViewHolder>() {


    val mustBorclariList = arrayListOf<MusteriVeresiye>()

    class BorclularViewHolder(val binding: MusteriBorclularListRowBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BorclularViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<MusteriBorclularListRowBinding>(inflater,R.layout.musteri_borclular_list_row ,parent,false)
        return BorclularViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BorclularViewHolder, position: Int) {
       val veresiye = mustBorclariList[position]
        holder.binding.veresiye= veresiye

        PictureUtil.alacaklarGorselIndir(veresiye.musteri,holder.itemView.context,holder.binding.musteriGorsel)
    }

    override fun getItemCount(): Int {
        return mustBorclariList.size
    }

    fun mustBorcListesiniGuncelle(yeniBorcListesi: List<MusteriVeresiye>) {

        mustBorclariList.clear()
        mustBorclariList.addAll(yeniBorcListesi)
        notifyDataSetChanged()

    }


}