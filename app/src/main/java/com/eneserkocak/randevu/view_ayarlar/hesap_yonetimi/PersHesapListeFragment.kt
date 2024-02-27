package com.eneserkocak.randevu.view_ayarlar.hesap_yonetimi

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.Util.UserUtil
import com.eneserkocak.randevu.adapter.PersHesapListeAdapter
import com.eneserkocak.randevu.databinding.DialogPersHesapListeBinding
import com.eneserkocak.randevu.model.FIRMA_KODU
import com.eneserkocak.randevu.model.PERSONELLER
import com.eneserkocak.randevu.model.Personel
import com.eneserkocak.randevu.viewModel.AppViewModel
import com.google.firebase.firestore.FirebaseFirestore


class PersHesapListeFragment : DialogFragment() {

    lateinit var binding: DialogPersHesapListeBinding
    val viewModel: AppViewModel by activityViewModels()

    var personelListesi= listOf<Personel>()
    val adapter=PersHesapListeAdapter(){
        viewModel.secilenPersonel.value=it
        findNavController().navigate(R.id.persHesapEkleFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogPersHesapListeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.persHesapListeRecycler.layoutManager=LinearLayoutManager(requireContext())
        binding.persHesapListeRecycler.adapter=adapter


        getData(){
            personelListesi = it
            adapter.persHesapListesiniGuncelle(it)
        }

    }

    fun getData(personeller: (List<Personel>)->Unit){

        FirebaseFirestore.getInstance().collection(PERSONELLER)
            .whereEqualTo(FIRMA_KODU, UserUtil.firmaKodu)
            .get().addOnSuccessListener {
                it?.let {
                    if (!it.isEmpty) {
                        val personelListesi =  it.toObjects(Personel::class.java)
                        personeller.invoke(personelListesi)
                    }

                }
                //println()
            }
            .addOnFailureListener {
                println(it)
            }


    }

    override fun getTheme(): Int {
        return R.style.persHesapListeFragmentFullScreenDialogStyle
    }
}