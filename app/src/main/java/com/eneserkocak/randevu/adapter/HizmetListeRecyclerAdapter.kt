package com.eneserkocak.randevu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.databinding.HizmetlisteRowBinding
import com.eneserkocak.randevu.model.*

import com.eneserkocak.randevu.view_ayarlar.hizmet_ayar.HizmetListeFragmentDirections
import com.google.firebase.firestore.FirebaseFirestore

class HizmetListeRecyclerAdapter(val secilenHizmet: (Hizmet)->Unit):RecyclerView.Adapter<HizmetListeRecyclerAdapter.HizmetDuzenleViewHolder>() {

    val hizmetListesi = arrayListOf<Hizmet>()

    class HizmetDuzenleViewHolder(val binding: HizmetlisteRowBinding) :RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HizmetDuzenleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<HizmetlisteRowBinding>(inflater,R.layout.hizmetliste_row,parent,false)
        return HizmetDuzenleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HizmetDuzenleViewHolder, position: Int) {
        val hizmet = hizmetListesi[position]
        holder.binding.hizmet = hizmet



        holder.itemView.setOnClickListener {


            secilenHizmet.invoke(hizmet)
            findNavController(it).popBackStack()
            findNavController(it).navigate(R.id.hizmetDuzenleFragment)
        }
        /*holder.binding.hizmetSilBtn.setOnClickListener {

             AppUtil.hizmetSil(hizmet,{})
            *//*if (count>0) {*//*
                hizmetListesi.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, hizmetListesi.size)
                AppUtil.longToast(holder.itemView.context,"Hizmet silindi..")
            }*/




    }

    override fun getItemCount(): Int {
        return hizmetListesi.size
    }

    fun hizmetListesiniGuncelle(yeniHizmetListesi: List<Hizmet>) {

        hizmetListesi.clear()
        hizmetListesi.addAll(yeniHizmetListesi)
        notifyDataSetChanged()
    }
}