package com.eneserkocak.randevu.view

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.UserUtil
import com.eneserkocak.randevu.databinding.ActivityMainBinding
import com.eneserkocak.randevu.model.FIRMALAR
import com.eneserkocak.randevu.model.KULLANICILAR

import com.eneserkocak.randevu.viewModel.AppViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity(),NavController.OnDestinationChangedListener {

    val viewModel : AppViewModel by viewModels()
    lateinit var binding: ActivityMainBinding

    lateinit var navController : NavController
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setNavController()

        checkPermissionLog()
        checkPermissionContacs()
        checkPermissionState()


        binding.cikisBtn.setOnClickListener {
            auth= FirebaseAuth.getInstance()
            auth.signOut()
            Navigation.findNavController(this,R.id.fragmentContainerView).navigate(R.id.authenticationFragment)
        }

        binding.uygulamaHakkindaBtn.setOnClickListener {
            Navigation.findNavController(this,R.id.fragmentContainerView).navigate(R.id.stepperInfoFragment)
        }

        binding.kullaniciRehberiBtn.setOnClickListener {
            Navigation.findNavController(this,R.id.fragmentContainerView).navigate(R.id.stepperFragment)
        }

        binding.whatsappBtn.setOnClickListener {


            val yoneticiTel = "+90${"05062352416"}"


            val url = "https://api.whatsapp.com/send?phone=$yoneticiTel"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)

        }

        //   replaceFragment(GirisFragment())

/*  binding.bottomNavigationView.setOnItemSelectedListener {


        when(it.itemId){

            R.id.anasayfa -> replaceFragment(GirisFragment())
            R.id.kullaniciRehberi-> replaceFragment(StepperFragment())
            R.id.uygulamaHakkinda-> replaceFragment(UygulamaHakkindaFragment())
            R.id.destek-> replaceFragment(DestekFragment())

            else->{

            }
        }
        true
    }*/


    }

    /*private fun replaceFragment(fragment:Fragment){

     val fragmentManager=requireActivity().supportFragmentManager
     val fragmentTransaction=fragmentManager.beginTransaction()
     fragmentTransaction.replace(R.id.FragmentContainerView,fragment)
     fragmentTransaction.commit()
 }*/

    fun setNavController(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        setSupportActionBar(binding.materialToolbar)
        setupActionBarWithNavController(navController) //title
        binding.materialToolbar.setupWithNavController(navController) // up
        navController.addOnDestinationChangedListener(this)

    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        binding.materialToolbar.setNavigationIcon(R.drawable.back)


       if (destination.id==R.id.girisFragment) binding.materialToolbar.setNavigationIcon(R.drawable.home_24)
        if (destination.id==R.id.authenticationFragment) binding.materialToolbar.visibility=View.GONE
        else binding.materialToolbar.visibility=View.VISIBLE

        if(destination.id==R.id.girisFragment) binding.girisDestination.visibility=View.VISIBLE
        else binding.girisDestination.visibility=View.GONE


    }

    private fun checkPermissionLog(){
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CALL_LOG),101)
        }
    }
    private fun checkPermissionContacs(){
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS),102)
        }
    }
    private fun checkPermissionState(){
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_PHONE_STATE),103)
        }
    }


}