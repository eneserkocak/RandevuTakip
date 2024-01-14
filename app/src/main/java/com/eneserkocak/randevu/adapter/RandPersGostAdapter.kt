package com.eneserkocak.randevu.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.Util.PictureUtil
import com.eneserkocak.randevu.databinding.RandevualPersonelRowBinding
import com.eneserkocak.randevu.model.ClickedButton

import com.eneserkocak.randevu.model.Personel

class RandPersGostAdapter(val secilenPersonel : (Personel)->Unit):RecyclerView.Adapter<RandPersGostAdapter.RandevualViewHolder>() {

    val personelListesi = arrayListOf<Personel>()
    private var selectedItem: Int?= null

    class RandevualViewHolder(val binding: RandevualPersonelRowBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandevualViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding= DataBindingUtil.inflate<RandevualPersonelRowBinding>(inflater, R.layout.randevual_personel_row,parent,false)
        return RandevualViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RandevualViewHolder, position:Int) {

        val personel= personelListesi[position]
        holder.binding.personel = personel
        PictureUtil.gorseliAl(personel,holder.itemView.context,holder.binding.persImage)


        //Tıklanabilir özelliği ve Click ile seçilince backround değiştirmek için:
        holder.itemView.setOnClickListener {
            secilenPersonel.invoke(personel)

                selectedItem=position
                notifyDataSetChanged()

        }

            if(selectedItem == position) {
              // holder.itemView.setBackgroundColor(Color.parseColor("#EBEBEB"))
                holder.binding.clickedPersonel.setBackgroundColor(Color.parseColor("#EBEBEB"))
            }else {
               // holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
                holder.binding.clickedPersonel.setBackgroundColor(Color.parseColor("#FFFFFF"))
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