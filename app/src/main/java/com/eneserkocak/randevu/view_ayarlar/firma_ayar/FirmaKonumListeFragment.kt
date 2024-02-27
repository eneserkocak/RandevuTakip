package com.eneserkocak.randevu.view_ayarlar.firma_ayar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.adapter.FirmaKonumListAdapter
import com.eneserkocak.randevu.databinding.DialogFirmaKonumListeBinding
import com.eneserkocak.randevu.databinding.DialogMusteriDuzenleBinding
import com.eneserkocak.randevu.db_firmaMaps.FirmaMapsDao
import com.eneserkocak.randevu.db_firmaMaps.FirmaMapsDatabase
import com.eneserkocak.randevu.model.Konum
import com.eneserkocak.randevu.viewModel.AppViewModel

const val MAPS_LISTE = "MapsListe"
const val FIRMA_AYAR_FRAGMENT = "FirmaAyarFragment"

class FirmaKonumListeFragment : DialogFragment() {

    lateinit var binding: DialogFirmaKonumListeBinding
    val viewModel: AppViewModel by activityViewModels()

    lateinit var dao: FirmaMapsDao
    private var firmaKonumList= listOf<Konum>()

    val adapter= FirmaKonumListAdapter(){
        viewModel.secilenKonum.value=it
        val action = FirmaKonumListeFragmentDirections.actionFirmaKonumListeFragmentToFirmaMapsFragment(MAPS_LISTE)
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFirmaKonumListeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        dao = FirmaMapsDatabase.getInstance(requireContext())!!.firmaMapsDao()
        firmaKonumList = dao.getAll()

        if (firmaKonumList.size==0) {
            AppUtil.longToast(requireContext(), "Firmanıza ait kayıtlı konum bulunamadı.Firma ayarlarından konum ekleyiniz..!")
            dismiss()
        } else {
            binding.firmaKonumListRecycler.visibility = View.VISIBLE
        }

        binding.firmaKonumListRecycler.layoutManager=LinearLayoutManager(requireContext())
        binding.firmaKonumListRecycler.adapter=adapter

        adapter.konumListGuncelle(firmaKonumList)


    }
    override fun getTheme(): Int {
        return R.style.firmaKonumListeFragmentFullScreenDialogStyle
    }

}