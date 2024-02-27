package com.eneserkocak.randevu.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.icu.text.Transliterator.Position
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.databinding.MusteriBorcRowBinding
import com.eneserkocak.randevu.model.*
import com.eneserkocak.randevu.viewModel.AppViewModel
import com.google.firebase.firestore.FirebaseFirestore

class MusteriVeresiyeAdapter(val viewModel: AppViewModel):RecyclerView.Adapter<MusteriVeresiyeAdapter.musteriVeresiyeViewHolder>() {

    val randevuList= ArrayList<Randevu>()

    class musteriVeresiyeViewHolder(val binding:MusteriBorcRowBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): musteriVeresiyeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding= DataBindingUtil.inflate<MusteriBorcRowBinding>(inflater, R.layout.musteri_borc_row,parent,false)
        return musteriVeresiyeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: musteriVeresiyeViewHolder,position:Int) {
        val randevu = randevuList[position]
        holder.binding.musteriRandevu = randevu


        holder.binding.switchBtn.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked){
                holder.binding.mustBorcDetayLayout.visibility= View.VISIBLE
            }else{
                holder.binding.mustBorcDetayLayout.visibility= View.GONE
            }

        }


        holder.binding.tahsilatBtn.setOnClickListener {

            tahsilatDialog(holder.itemView.context,randevu,position,it)
        }



    }

    override fun getItemCount(): Int {
        return randevuList.size
    }

    fun musteriVeresiyeListesiniGuncelle(yeniMustVeresiyeListesi: List<Randevu>) {

        randevuList.clear()
        randevuList.addAll(yeniMustVeresiyeListesi)
        notifyDataSetChanged()


    }

    fun tahsilatDialog(context: Context,randevu: Randevu,position:Int,view: View){

        val alert = AlertDialog.Builder(context)
        alert.setMessage("Müşteriye ait seçilen borç silinecek...                            Onaylıyormusunuz ?")

        alert.setPositiveButton("EVET", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {

                val randevuGelirTuru= NAKIT
                val veresiyeTutari=0

                val randevuMap = mapOf<String,Any>(

                    RANDEVU_GELIR_TURU to randevuGelirTuru,
                    VERESIYE_TUTARI to veresiyeTutari
                )

                FirebaseFirestore.getInstance().collection(RANDEVULAR).document(AppUtil.randevuDocumentPath(randevuList.get(position)))
                    .update(randevuMap)
                    .addOnSuccessListener {

                        viewModel.veresiyeTamamlananRandevuListesi.value!!.find { it.randevuId==randevu.randevuId }
                            ?.apply {

                                this.randevuGelirTuru=randevuGelirTuru
                                this.veresiyeTutari=veresiyeTutari
                            }
                        viewModel.veresiyeTamamlananRandevuListesi.value =  viewModel.veresiyeTamamlananRandevuListesi.value


                        randevuList.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, randevuList.size)
                        notifyDataSetChanged()

    //TAHSİLAT yapınca ilk row siliniyor..sonrakiler siliniyo fakat KAYBOLMUYORDU..AŞAĞIDA Fragment KAPAT-AÇ YAPINA DÜZELDİ.
                        Navigation.findNavController(view).popBackStack()
                        Navigation.findNavController(view).navigate(R.id.musteriCariFragment)

                    }

                AppUtil.longToast(context,"Tahsilat Yapıldı.")
                dialog!!.dismiss()


            }
        })


        alert.setNegativeButton("HAYIR", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                // findNavController().popBackStack()
            }
        })

        alert.show()


    }



}