package com.eneserkocak.randevu.view_randevuListesi

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.Util.toSaat
import com.eneserkocak.randevu.Util.toTarih
import com.eneserkocak.randevu.Util.toTimestamp
import com.eneserkocak.randevu.databinding.DialogRandevuNotlarBinding
import com.eneserkocak.randevu.databinding.DialogSmsGonderBinding
import com.eneserkocak.randevu.model.Randevu

import com.eneserkocak.randevu.view.BaseFragment
import com.eneserkocak.randevu.viewModel.AppViewModel
import java.text.SimpleDateFormat
import java.util.*


class SmsGonderFragment : DialogFragment() {

    lateinit var binding: DialogSmsGonderBinding
    val viewModel: AppViewModel by activityViewModels()
    lateinit var randevu:Randevu

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogSmsGonderBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkPermission()

    //WHATSAPP MESAJI GÖNDER:

        viewModel.secilenRandevu.observe(viewLifecycleOwner){
            it?.let {

                    randevu=it


        binding.dialogWhtspSmsBtn.setOnClickListener {

    //RANDEVU tarihlerini Timestamp ten String olarak parçala al:
                val cal= Calendar.getInstance()
                cal.time=randevu.randevuTime.toDate()
                val sdf = SimpleDateFormat("dd-MM-yyyy,E")
                val randevuTarih = sdf.format(cal.time)

                val saatSdf = SimpleDateFormat("HH:mm")
                val randevuSaat= saatSdf.format(cal.time)

                val musteriAdi=randevu.musteri.musteriAdi.uppercase()
                val musteriTel= "+90${randevu.musteri.musteriTel}"

            if(musteriTel.length==14){

                val url = "https://api.whatsapp.com/send?phone=$musteriTel&text=Sayın $musteriAdi, $randevuTarih günü, saat: $randevuSaat 'da randevu kaydınız oluşturulmuştur.Bizi tercih ettiğiniz için teşekkür eder, İyi günler dileriz."
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                context?.startActivity(i)

                dismiss()

                AppUtil.longToast(requireContext(),"Müşteriye whatsapp bilgi mesajı gönderiliyor..!")

            }else{
                AppUtil.longToast(requireContext(),"Müşteriye ait telefon numarası kayıtlı değil veya hatalı kaydedilmiş.")
            }


      }

    //TELEFON SMS GÖNDER

        binding.dialogSmsBtn.setOnClickListener {

                    //RANDEVU tarihlerini Timestamp ten String olarak parçala al:
                    val cal= Calendar.getInstance()
                    cal.time=randevu.randevuTime.toDate()
                    val tarihSdf = SimpleDateFormat("dd-MM-yyyy,E")
                    val randTarih = tarihSdf.format(cal.time)

                    val saattSdf = SimpleDateFormat("HH:mm")
                    val randSaat= saattSdf.format(cal.time)

                    val mustAdi=randevu.musteri.musteriAdi.uppercase()
                    val mustTel= "${randevu.musteri.musteriTel}"
                    val smsMesaj= "Sayın $mustAdi, $randTarih günü, saat: $randSaat 'da randevu kaydınız oluşturulmuştur.Bizi tercih ettiğiniz için teşekkür eder, İyi günler dileriz."

            if(mustTel.length==11){
                   val smsIntent=Intent(Intent.ACTION_VIEW)

                       // smsIntent.setType("vnd.android-dir/mms-sms")
                        smsIntent.data = Uri.parse("sms:$mustTel")
                        smsIntent.putExtra("sms_body", "$smsMesaj")
                        startActivity(smsIntent)




                    dismiss()

                    AppUtil.longToast(requireContext(),"Müşteriye sms bilgi mesajı gönderiliyor..!")
            }else{
                AppUtil.longToast(requireContext(),"Müşteriye ait telefon numarası kayıtlı değil veya hatalı kaydedilmiş.")
            }

//Sayın $musteriAdi, $randevuTarih günü, saat: $randevuSaat 'da randevu kaydınız oluşturulmuştur.Bizi tercih ettiğiniz için teşekkür eder, İyi günler dileriz.

                 }

                binding.vazgecBtn.setOnClickListener {
                    findNavController().popBackStack()
                }
            }
        }

    }

    private fun checkPermission(){
        if (ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.SEND_SMS),101)

        }
    }

    override fun getTheme(): Int {
        return R.style.smsGonderFragmentFullScreenDialogStyle
    }

}