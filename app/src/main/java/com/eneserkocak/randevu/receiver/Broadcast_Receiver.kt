package com.eneserkocak.randevu.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import com.eneserkocak.randevu.view.CallerActivity
import com.google.firebase.auth.FirebaseAuth


class Broadcast_Receiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {


        val state= intent?.getStringExtra(TelephonyManager.EXTRA_STATE)

            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)){

               // Toast.makeText(context,"Gelen çağrı",Toast.LENGTH_LONG).show()

            val incomingCallerNumber= intent?.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                if (incomingCallerNumber!=null){

      //KULLANICI SIGN OUT İLE ÇIKTIYSA DİALOGLARI GÖSTERME

                    val currentUser=FirebaseAuth.getInstance().currentUser ?: return

                    val intent = Intent(context, CallerActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.putExtra("number",incomingCallerNumber)
                   // intent.putExtra("name",arayanMusteriAdi)
                    context?.startActivity(intent)
                }


                    /*val gelenNumara = incomingCallerNumber?.subSequence(2, 13).toString()
                    FirebaseFirestore.getInstance().collection(MUSTERILER)
                        .whereEqualTo(FIRMA_KODU, UserUtil.firmaKodu)
                        .get()
                        .addOnSuccessListener {
                            it?.let {
                                val mustList = it.toObjects(Musteri::class.java)


                                var arayanMustList = listOf<Musteri>()
                                arayanMustList = mustList.filter {
                                    it?.let {
                                        it.musteriTel == gelenNumara
                                    }!!
                                }

                                if (arayanMustList.isNotEmpty()){
                                val arayanMusteri= arayanMustList.get(0)
                                val arayanMusteriAdi= arayanMusteri.musteriAdi

                                    val intent = Intent(context, CallerActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    intent.putExtra("number",incomingCallerNumber)
                                    intent.putExtra("name",arayanMusteriAdi)
                                    context?.startActivity(intent)

                            }else{

                                val intent = Intent(context, CallerActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                intent.putExtra("number",incomingCallerNumber)
                                intent.putExtra("name","KAYIT YOK")
                                context?.startActivity(intent)
                               // AppUtil.shortToast(context,"KAYIT YOK")
                            }


                            }
                        }*/
               }}}







