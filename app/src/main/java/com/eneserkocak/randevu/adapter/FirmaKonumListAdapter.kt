package com.eneserkocak.randevu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.databinding.KonumlisteRowBinding
import com.eneserkocak.randevu.databinding.RecyclerRowGunlerBinding
import com.eneserkocak.randevu.model.Konum

class FirmaKonumListAdapter(val secilenKonum : (Konum)->Unit):RecyclerView.Adapter<FirmaKonumListAdapter.KonumViewHolder>() {

    var konumList= arrayListOf<Konum>()
    class KonumViewHolder(val binding:KonumlisteRowBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KonumViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<KonumlisteRowBinding>(inflater,R.layout.konumliste_row,parent,false)
        return KonumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KonumViewHolder, position: Int) {

        val konum = konumList[position]
        holder.binding.konum=konum

        holder.itemView.setOnClickListener {

            secilenKonum.invoke(konum)
        }

    }

    override fun getItemCount(): Int {
       return konumList.size
    }

    fun konumListGuncelle (yeniKonumList:List<Konum>){

        konumList.clear()
        konumList.addAll(yeniKonumList)
        notifyDataSetChanged()

    }



}