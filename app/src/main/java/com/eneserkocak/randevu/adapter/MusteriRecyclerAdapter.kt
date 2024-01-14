package com.eneserkocak.randevu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.databinding.MusteriRowBinding
import com.eneserkocak.randevu.model.Musteri
import com.eneserkocak.randevu.view_musteri.MusteriFragmentDirections


class MusteriRecyclerAdapter(val secilenMusteri: (Musteri)->Unit):RecyclerView.Adapter<MusteriRecyclerAdapter.MusteriViewHolder>() {

    val musteriListesi= ArrayList<Musteri>()


    class MusteriViewHolder(val binding:MusteriRowBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusteriViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding= DataBindingUtil.inflate<MusteriRowBinding>(inflater, R.layout.musteri_row,parent,false)
        return MusteriRecyclerAdapter.MusteriViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MusteriViewHolder, position: Int) {


        val musteri = musteriListesi[position]
        holder.binding.musteri= musteri

        holder.itemView.setOnClickListener {

            secilenMusteri.invoke(musteri)

            /*val action= MusteriFragmentDirections.actionMusteriFragmentToMusteriDuzenleFragment()
            Navigation.findNavController(it).navigate(action)*/
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
