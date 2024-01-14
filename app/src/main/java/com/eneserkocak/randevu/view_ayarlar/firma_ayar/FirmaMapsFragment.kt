package com.eneserkocak.randevu.view_ayarlar.firma_ayar

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.databinding.FragmentFirmaMapsBinding
import com.eneserkocak.randevu.db_firmaMaps.FirmaMapsDatabase
import com.eneserkocak.randevu.model.*
import com.eneserkocak.randevu.view.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar


class FirmaMapsFragment : BaseFragment<FragmentFirmaMapsBinding>(R.layout.fragment_firma_maps),OnMapReadyCallback,GoogleMap.OnMapLongClickListener {


    private lateinit var mMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var sharedPreferences: SharedPreferences
    var trackBoolean: Boolean? = null
    private var selectedLatitude: Double? = null
    private var selectedLongitude: Double? = null
    lateinit var gelinenYer : String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)


        registerLauncher()


        sharedPreferences = requireActivity().getSharedPreferences("com.eneserkocak",AppCompatActivity.MODE_PRIVATE)
        trackBoolean = false




        binding.saveButton.setOnClickListener {

            val firmaAdi=binding.placeText.text.toString()
            val firmaLat=selectedLatitude!!
            val firmaLng=selectedLongitude!!

            val firmaninKonumu= Konum(firmaAdi,firmaLat,firmaLng)
            viewModel.secilenKonum.value=firmaninKonumu
            println(firmaninKonumu)
            FirmaMapsDatabase.getInstance(requireContext())?.firmaMapsDao()?.insertAll(firmaninKonumu)


            findNavController().popBackStack()
            AppUtil.longToast(requireContext(),"Konum kaydedildi. Eklenen konumlar, Müşteriler -> Konum Gönder menüsünde görülür.. ")


            //Firmanın  birden fazla konumu oabilir.Bu nedenle SHARED İPTAL->  ROOM LA KAYDEDECEĞİM::::
           /* val sharedPreferences = AppUtil.getSharedPreferences(requireContext())

            sharedPreferences.edit().putString(FIRMA_ISIM,firmaAdi).apply()
            sharedPreferences.edit().putString(FIRMA_LAT,firmaLat.toString()).apply()
            sharedPreferences.edit().putString(FIRMA_LNG,firmaLng.toString()).apply()*/

        }

        binding.deleteButton.setOnClickListener {
            viewModel.secilenKonum.value?.let {
              //  FirmaMapsDatabase.getInstance(requireContext())?.firmaMapsDao()?.delete(it)
              //  findNavController().popBackStack()
            konumSilDialog()

            }
        }

        binding.konumGonderBtn.setOnClickListener {

            viewModel.secilenKonum.value?.let {
                val lat=it.latitude.toString()
                val long=it.longitude.toString()
                val firmaIsim=it.firmaAdi


                viewModel.secilenMusteri.observe(viewLifecycleOwner){
                    it?.let {
                        val musteriTel= "+90${it.musteriTel}"
                       val url = "https://api.whatsapp.com/send?phone=$musteriTel&text=https://www.google.com/maps/?q=$lat,$long"
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse(url)
                        context?.startActivity(i)


                       /* val uri = "https://www.google.com/maps/?q=$lat,$long"
                        val sharingIntent = Intent(Intent.ACTION_SEND)
                        sharingIntent.type = "text/plain"
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, uri)
                        startActivity(Intent.createChooser (sharingIntent, "${firmaIsim}"))*/


                    }
                }

            }
        }




        }
    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        mMap.setOnMapLongClickListener(this)

        arguments?.let {
            gelinenYer = FirmaMapsFragmentArgs.fromBundle(it).gelinenSayfa
        }
        if (gelinenYer== MAPS_LISTE) {
            binding.placeText.visibility = View.GONE
            binding.saveButton.visibility = View.GONE
            binding.deleteButton.visibility=View.VISIBLE
            binding.konumGonderBtn.visibility=View.VISIBLE

            viewModel.secilenKonum.observe(viewLifecycleOwner) {
                it?.let {
                    mMap.addMarker(MarkerOptions().position(LatLng(it.latitude, it.longitude)).title(it.firmaAdi)                    )
                    mMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(it.latitude,it.longitude), 15f))

                    binding.placeText.setText(it.firmaAdi)

                }
            }
            return
        }

        binding.deleteButton.visibility=View.GONE
        binding.saveButton.visibility=View.VISIBLE
        binding.konumGonderBtn.visibility=View.GONE


        locationManager =requireContext().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager


        locationListener = object : LocationListener {override fun onLocationChanged(location: Location) {

            println(location.toString())

            trackBoolean = sharedPreferences.getBoolean("trackBoolean", false)
            if (trackBoolean == false) {

                val userLocation = LatLng(location.latitude, location.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))

                sharedPreferences.edit().putBoolean("trackBoolean", true).apply()

            }

        }

            override fun onProviderDisabled(provider: String) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

        }

        if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.ACCESS_FINE_LOCATION)
            ) {
                Snackbar.make( binding.root,"Permission needed for location",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission") {
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

                }.show()

            } else {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }


        } else {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,0,0f, locationListener
            )

            val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastLocation != null) {
                val lastUserLocation = LatLng(lastLocation.latitude, lastLocation.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 15f))
            }
            mMap.isMyLocationEnabled = true

        }

    }


    private fun registerLauncher() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    //permission granted (izin verildi),(Yukarıda izin verilen yeri buraya da kopyala Aynı şeyi yapıp Konum isteyeceğiz)
                    if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,locationListener)

                        val lastLocation =locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        if (lastLocation != null) {
                            val lastUserLocation =LatLng(lastLocation.latitude, lastLocation.longitude)
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,15f))}
                        mMap.isMyLocationEnabled = true

                    }


                } else {
                    Toast.makeText(requireContext(), "İzin vermeniz gerekiyor!", Toast.LENGTH_LONG).show()

                }
            }
    }

    override fun onMapLongClick(p0: LatLng) {

        mMap.clear()

        mMap.addMarker(MarkerOptions().position(p0))

        selectedLatitude = p0.latitude
        selectedLongitude = p0.longitude

         println("selectedLatitude"+":"+selectedLatitude)
    }


    fun konumSilDialog(){

        val alert = AlertDialog.Builder(requireContext())
        alert.setMessage("Konum silinsin mi?")

        alert.setPositiveButton("EVET", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                //ROOM dan silme işlemi yap
                viewModel.secilenKonum.value?.let {
                      FirmaMapsDatabase.getInstance(requireContext())?.firmaMapsDao()?.delete(it)
                }

                AppUtil.longToast(requireContext(),"Konum silindi")
                findNavController().popBackStack()
                findNavController().navigate(R.id.firmaKonumListeFragment)

            }
        })
        alert.setNegativeButton("HAYIR", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
               // findNavController().popBackStack()
            }
        })
        alert.show()
    }

     /*fun firmaKonumEkle(konum: Konum) {

        konum.let {
            val konumHashMap = hashMapOf<String, Any>()

            konumHashMap.put("firmaAdi", it.firmaAdi)
            konumHashMap.put("firmaEnlem", it.latitude.toString())
            konumHashMap.put("firmaBoylam", it.longitude.toString())



            println(konumHashMap)
            FirebaseFirestore.getInstance().collection(KONUM).document(it.firmaAdi).set(konumHashMap)
                .addOnSuccessListener {

                    println()
                }
                .addOnFailureListener {
                    println(it)
                }
        }

    }*/




}








