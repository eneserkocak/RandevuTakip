package com.eneserkocak.randevu.view_ayarlar.personel_ayar

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isNotEmpty
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.Util.PictureUtil
import com.eneserkocak.randevu.adapter.MusteriDuzRandAdapter
import com.eneserkocak.randevu.adapter.PersonelRandevuAdapter
import com.eneserkocak.randevu.databinding.DialogMusteriDuzenleBinding
import com.eneserkocak.randevu.databinding.DialogPersRandevularBinding
import com.eneserkocak.randevu.model.Musteri
import com.eneserkocak.randevu.model.Personel
import com.eneserkocak.randevu.viewModel.AppViewModel
import com.eneserkocak.randevu.view_musteri.filtreRandevuListesi


class PersonelRandevularFragment : DialogFragment() {

    lateinit var binding: DialogPersRandevularBinding
    val viewModel: AppViewModel by activityViewModels()

    lateinit var secilenPersonel: Personel

    val adapter= PersonelRandevuAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogPersRandevularBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.secilenPersonel.observe(viewLifecycleOwner) {
            it?.let {
                secilenPersonel = it
                PictureUtil.gorseliAl(it, requireContext(), binding.personelGorsel)

                binding.personelAdiText.text=it.personelAdi
                binding.personelTelText.text=it.personelTel
                binding.personelMailText.text=it.personelMail
            }
        }

        binding.personelRandevuRecycler.layoutManager=LinearLayoutManager(requireContext())
        binding.personelRandevuRecycler.adapter= adapter



        viewModel.randVerileriGetir()
        viewModel.randevuListesi.observe(viewLifecycleOwner){
            it?.let {

                filtreRandevuListesi = it.filter { it.personel.personelAdi==secilenPersonel.personelAdi}


                if (filtreRandevuListesi.isNotEmpty()){
                    binding.personelRandevuRecycler.visibility=View.VISIBLE
                    binding.randevularText.visibility=View.VISIBLE
                    adapter.personelRandevuListesiniGuncelle(filtreRandevuListesi)
                }else{
                    binding.personelRandevuRecycler.visibility=View.GONE
                    binding.randevularText.visibility=View.GONE
                }
            }
        }



        binding.persBlgDznlBtn.setOnClickListener {
            findNavController().navigate(R.id.perDuzenleFragment)
        }
        binding.personelSilBtn.setOnClickListener {
            personelSilDialog()
        }


    }


        override fun getTheme(): Int {
            return R.style.personelRandevularFragmentFullScreenDialogStyle
        }

    fun personelSilDialog(){

        val alert = AlertDialog.Builder(requireContext())
        // alert.setMessage("Müşteri silinsin mi?")

        val personelAdi= secilenPersonel.personelAdi.uppercase()
        if (binding.personelRandevuRecycler.isNotEmpty()){

            alert.setMessage("${personelAdi} isimli personelin kayıtlı geçmiş randevu bilgisi bulunmaktadır. Randevusu bulunan personeli silemezsiniz...!           Personele ait RANDEVULAR bölümünden, geçmiş randevularını sildikten sonra personeli silebilirsiniz..")
            alert.setTitle("UYARI..!")
           // return

        }else{
            alert.setMessage("Personel silinsin mi?")


        alert.setPositiveButton("EVET", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                //Firebase den silme işlemi yap
                AppUtil.personelSil(secilenPersonel,{})
                PictureUtil.gorseliSil(secilenPersonel,requireContext(),{})

                AppUtil.longToast(requireContext(),"Personel silindi")
                findNavController().popBackStack()


                //Aşğıdaki navigate i koymayınca dialog kapanıyor fakat liste fragment ta çık gir yapmadan müşt silinmiyor
                findNavController().navigate(R.id.personelListeFragment)

            }
        })
        alert.setNegativeButton("HAYIR", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                findNavController().popBackStack()
            }
        })
     }
        alert.show()
  }


}