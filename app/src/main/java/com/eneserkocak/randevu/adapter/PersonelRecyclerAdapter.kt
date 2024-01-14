package com.eneserkocak.randevu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.databinding.PersonelRowBinding
import com.eneserkocak.randevu.model.Personel

import com.eneserkocak.randevu.view_ayarlar.personel_ayar.PersonelListeFragmentDirections


class PersonelRecyclerAdapter(val secilenPersonel: (Personel)->Unit):RecyclerView.Adapter<PersonelRecyclerAdapter.PersonelViewHolder>() {

    val personelListesi = arrayListOf<Personel>()

    class PersonelViewHolder(val binding:PersonelRowBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonelViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding= DataBindingUtil.inflate<PersonelRowBinding>(inflater, R.layout.personel_row,parent,false)
        return PersonelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonelViewHolder, position: Int) {

        val personel = personelListesi[position]
        holder.binding.personel= personel

        holder.itemView.setOnClickListener {

            secilenPersonel.invoke(personel)

            val action =PersonelListeFragmentDirections.actionPersonelListeFragmentToPersonelRandevularFragment()
            Navigation.findNavController(it).navigate(action)
        }


    }

    override fun getItemCount(): Int {
        return personelListesi.size
    }

    fun personelListesiniGuncelle(yeniPersonelListesi: List<Personel>){

        personelListesi.clear()
        personelListesi.addAll(yeniPersonelListesi)
        notifyDataSetChanged()


    }
}