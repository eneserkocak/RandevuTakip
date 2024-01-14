package com.eneserkocak.randevu.adapter

import android.graphics.Color
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.databinding.RandevualSaatRowBinding
import com.eneserkocak.randevu.model.CalismaGun
import com.eneserkocak.randevu.model.ClickedButton
import com.eneserkocak.randevu.model.RandevuSaati
import java.text.SimpleDateFormat

//İLK ÖNCE BASE ADAPTER OLARAK YAZDIK..ANCAK-> BASE ADAPTER OLD İÇİN itemView.setOnClickLstener ÇALIŞMIYOR
//BU NEDENLE SEÇİLEN ITEM BACKROUND DEĞİŞMİYOR...NORMAL ADAPTER E ÇEVİRDİM.

/*
class RandSaatGostAdapter(clickedItem: (RandevuSaati) -> Unit) :BaseAdapter<RandevuSaati,RandevualSaatRowBinding>(
    clickedItem)

{  var selectedItem: Int?= null

    override fun getLayoutRes(): Int {
        return R.layout.randevual_saat_row
      }



    override fun bindViewHolder(
        holder: BaseViewHolder<RandevualSaatRowBinding>,
        item: RandevuSaati,
        position: Int
    ) {
        val sdf = SimpleDateFormat("HH:mm")
        holder.binding.saatTextV.text = sdf.format(item.saat)


        holder.itemView.setOnClickListener {


            selectedItem=position
            notifyDataSetChanged()

        }
        if(selectedItem == position) {
            // holder.itemView.setBackgroundColor(Color.parseColor("#EBEBEB"))
            holder.binding.clickedSaat.setBackgroundColor(Color.parseColor("#EBEBEB"))
        }else {
           // holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
            holder.binding.clickedSaat.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }


    }
}

*/
