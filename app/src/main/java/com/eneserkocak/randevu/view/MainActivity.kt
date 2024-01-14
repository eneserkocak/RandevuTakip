package com.eneserkocak.randevu.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.databinding.ActivityMainBinding

import com.eneserkocak.randevu.viewModel.AppViewModel

class MainActivity : AppCompatActivity(),NavController.OnDestinationChangedListener {

    val viewModel : AppViewModel by viewModels()
    lateinit var binding: ActivityMainBinding
    lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setNavController()

    }

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
        binding.materialToolbar.setNavigationIcon(R.drawable.ilac_detay_back)


        if (destination.id==R.id.girisFragment) binding.materialToolbar.setNavigationIcon(R.drawable.home_24)
        if (destination.id==R.id.authenticationFragment) binding.materialToolbar.visibility=View.GONE
        else binding.materialToolbar.visibility=View.VISIBLE


    }


}