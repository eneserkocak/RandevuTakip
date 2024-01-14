package com.eneserkocak.randevu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.databinding.GunsaatPersListRowBinding
import com.eneserkocak.randevu.model.Hizmet
import com.eneserkocak.randevu.model.Personel
import com.eneserkocak.randevu.view_ayarlar.gun_saat_ayar.GunSaatPersListeFragmentDirections

class GunSaatPersListAdapter(val secilenPersonel: (Personel)->Unit):RecyclerView.Adapter<GunSaatPersListAdapter.GunSaatPersListViewHolder>() {

    val personelList= arrayListOf<Personel>()

    class GunSaatPersListViewHolder(val binding: GunsaatPersListRowBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GunSaatPersListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<GunsaatPersListRowBinding>(inflater,R.layout.gunsaat_pers_list_row,parent,false)
        return GunSaatPersListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GunSaatPersListViewHolder, position: Int) {

        val personel= personelList[position]
        holder.binding.personel=personel

    holder.itemView.setOnClickListener {
        secilenPersonel.invoke(personel)
        val action=GunSaatPersListeFragmentDirections.actionGunSaatPersListeFragmentToGunSaatDuzenleFragment()
        Navigation.findNavController(it).navigate(action)
    }



    }

    override fun getItemCount(): Int {
        return personelList.size
    }

    fun personelListesiniGuncelle(yeniPersonelListesi: List<Personel>) {

        personelList.clear()
        personelList.addAll(yeniPersonelListesi)
        notifyDataSetChanged()
    }
}