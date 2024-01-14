package com.eneserkocak.randevu.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.Util.AppUtil
import com.eneserkocak.randevu.databinding.FragmentAuthenticationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class AuthenticationFragment : BaseFragment<FragmentAuthenticationBinding>(R.layout.fragment_authentication) {


    private lateinit var auth :FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth= Firebase.auth

        val currentUser= auth.currentUser
        if (currentUser != null){
        findNavController().popBackStack()
        findNavController().navigate(R.id.girisFragment)
     }

        binding.kayitOlBtn.setOnClickListener {

           val email= binding.epostaText.text.toString().trim()
           val password= binding.parolaText.text.toString()


           if (email.equals("") || password.equals("")){
               Toast.makeText(requireContext(),"Enter email and password!",Toast.LENGTH_LONG).show()

           }else{

               auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {

                  findNavController().popBackStack()
                  findNavController().navigate(R.id.girisFragment)


               }.addOnFailureListener {

                   Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
               }
           }
       }
        binding.girisYapBtn.setOnClickListener {
            val email= binding.epostaText.text.toString().trim()
            val password= binding.parolaText.text.toString()


            if (email.equals("") || password.equals("")){
                Toast.makeText(requireContext(),"Enter email and password!", Toast.LENGTH_LONG).show()
            }else{
                auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                    findNavController().popBackStack()
                    findNavController().navigate(R.id.girisFragment)
                }.addOnFailureListener {
                    Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()

                }
            }
        }
     }
 }

