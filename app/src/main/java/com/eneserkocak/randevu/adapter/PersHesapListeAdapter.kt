package com.eneserkocak.randevu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.databinding.DialogPersHesapListeBinding
import com.eneserkocak.randevu.databinding.PersonelHesapListeRowBinding
import com.eneserkocak.randevu.databinding.PersonelRowBinding
import com.eneserkocak.randevu.model.Personel
import com.eneserkocak.randevu.view_ayarlar.gun_saat_ayar.GunSaatPersListeFragmentDirections
import com.eneserkocak.randevu.view_ayarlar.hesap_yonetimi.PersHesapListeFragmentDirections
import com.eneserkocak.randevu.view_ayarlar.personel_ayar.PersonelListeFragmentDirections

class PersHesapListeAdapter(val secilenPersonel: (Personel)->Unit): RecyclerView.Adapter<PersHesapListeAdapter.PersHesapListeViewHolder>() {

    val personelListesi = arrayListOf<Personel>()

    class PersHesapListeViewHolder(val binding: PersonelHesapListeRowBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersHesapListeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding= DataBindingUtil.inflate<PersonelHesapListeRowBinding>(inflater, R.layout.personel_hesap_liste_row,parent,false)
        return PersHesapListeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersHesapListeViewHolder, position: Int) {

        val personel=personelListesi[position]
        holder.binding.personel=personel

        holder.itemView.setOnClickListener {
            secilenPersonel.invoke(personel)

            //Navigation burdan çalışmıyor.PersHesapListeFragment taki adapterden yap
          // Navigation.findNavController(it).navigate(R.id.persHesapEkleFragment)

        }
    }

    override fun getItemCount(): Int {
        return personelListesi.size
    }

    fun persHesapListesiniGuncelle(yeniPersListesi: List<Personel>){

        personelListesi.clear()
        personelListesi.addAll(yeniPersListesi)
        notifyDataSetChanged()


    }
}