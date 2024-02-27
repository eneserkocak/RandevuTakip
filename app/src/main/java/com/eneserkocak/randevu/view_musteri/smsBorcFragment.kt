package com.eneserkocak.randevu.view_musteri

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
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
import com.eneserkocak.randevu.Util.UserUtil
import com.eneserkocak.randevu.databinding.DialogSmsBorcBinding
import com.eneserkocak.randevu.databinding.DialogSmsGonderBinding
import com.eneserkocak.randevu.model.Musteri
import com.eneserkocak.randevu.model.Randevu
import com.eneserkocak.randevu.viewModel.AppViewModel
import java.text.SimpleDateFormat
import java.util.*

class smsBorcFragment : DialogFragment() {

    lateinit var binding:DialogSmsBorcBinding
    val viewModel: AppViewModel by activityViewModels()
    lateinit var musteri: Musteri


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogSmsBorcBinding.inflate(inflater,container,false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        checkPermission()


        //WHATSAPP MESAJI GÖNDER:

        viewModel.secilenMusteri.observe(viewLifecycleOwner){
            it?.let {

                musteri=it

                binding.whatsappSmsBtn.setOnClickListener {



                    val musteriAdi=musteri.musteriAdi.uppercase()
                    val musteriTel= "+90${musteri.musteriTel}"

                    val firmaAdi= UserUtil.firmaIsim.uppercase()

                    val musteriBorc= musteri.musteriBorc

                    if(musteriTel.length==14){

                        val url = "https://api.whatsapp.com/send?phone=$musteriTel&text=Sayın $musteriAdi, hesabınıza ait, $musteriBorc TL borç bakiyeniz bulunmaktadır... $firmaAdi"
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

                binding.telefonSmsBtn.setOnClickListener {

                    val musteriBorc= musteri.musteriBorc

                    val mustAdi=musteri.musteriAdi.uppercase()
                    val mustTel= "${musteri.musteriTel}"

                    val firmaAdi= UserUtil.firmaIsim.uppercase()

                    val smsMesaj= "Sayın $mustAdi, hesabınıza ait, $musteriBorc TL borç bakiyeniz bulunmaktadır... $firmaAdi"



                    if(mustTel.length==11){
                        val smsIntent= Intent(Intent.ACTION_VIEW)

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
        return R.style.smsBorcFragmentFullScreenDialogStyle
    }

}