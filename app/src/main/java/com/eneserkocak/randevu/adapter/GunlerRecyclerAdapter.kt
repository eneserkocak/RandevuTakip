package com.eneserkocak.randevu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.TimeUtil
import com.eneserkocak.randevu.databinding.RecyclerRowGunlerBinding
import com.eneserkocak.randevu.model.CalismaGun

class GunlerRecyclerAdapter( val parentFragmentManager:FragmentManager):RecyclerView.Adapter<GunlerRecyclerAdapter.GunlerViewHolder>() {

    var gunList= mutableListOf<CalismaGun>()


    class GunlerViewHolder(val binding: RecyclerRowGunlerBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GunlerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<RecyclerRowGunlerBinding>(inflater,R.layout.recycler_row_gunler,parent,false)
        return GunlerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GunlerViewHolder, position: Int) {

        val gun=gunList[position]
        holder.binding.gun= gun

        holder.binding.baslangicSaat.setOnClickListener {
            TimeUtil.showTimerPickerFragment(holder.itemView.context,parentFragmentManager){

            gun.baslangicSaat=it
              notifyItemChanged(position)

            }
        }
        holder.binding.bitisSaat.setOnClickListener {
            TimeUtil.showTimerPickerFragment(holder.itemView.context,parentFragmentManager){

               gun.bitisSaat=it
                notifyItemChanged(position)
            }
        }

        holder.binding.switchBtn.setOnCheckedChangeListener { buttonView, isChecked ->
                gun.calisiyormu = isChecked
        }
    }

    override fun getItemCount(): Int {
       return gunList.size
    }

    fun gunListesiniGuncelle(yeniGunListesi: List<CalismaGun>){

        gunList.clear()
        gunList.addAll(yeniGunListesi)
        notifyDataSetChanged()
    }
}
