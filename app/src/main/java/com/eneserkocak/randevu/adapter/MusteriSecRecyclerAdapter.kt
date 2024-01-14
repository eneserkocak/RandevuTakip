package com.eneserkocak.randevu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.databinding.DialogMusteriSecBinding
import com.eneserkocak.randevu.databinding.MusteriRowBinding
import com.eneserkocak.randevu.databinding.MusteriSecRowBinding
import com.eneserkocak.randevu.model.Musteri

class MusteriSecRecyclerAdapter(val secilenMusteri: (Musteri)->Unit):RecyclerView.Adapter<MusteriSecRecyclerAdapter.MusteriSecViewHolder>() {

    val musteriListesi= ArrayList<Musteri>()

    class MusteriSecViewHolder(val binding: MusteriSecRowBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusteriSecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding= DataBindingUtil.inflate<MusteriSecRowBinding>(inflater, R.layout.musteri_sec_row,parent,false)
        return MusteriSecViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MusteriSecViewHolder, position: Int) {

        val musteri=musteriListesi[position]
        holder.binding.musteri= musteri

        holder.itemView.setOnClickListener {
            secilenMusteri.invoke(musteri)
        }

    }

    override fun getItemCount(): Int {
       return musteriListesi.size
    }

    fun musteriListesiniGuncelle(yeniMusteriListesi: List<Musteri>){

        musteriListesi.clear()
        musteriListesi.addAll(yeniMusteriListesi)
        notifyDataSetChanged()


    }
}