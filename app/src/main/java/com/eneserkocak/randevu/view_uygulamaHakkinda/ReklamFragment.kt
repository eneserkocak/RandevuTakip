package com.eneserkocak.randevu.view_uygulamaHakkinda

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.databinding.FragmentReklamBinding
import com.eneserkocak.randevu.view.BaseFragment


class ReklamFragment : BaseFragment<FragmentReklamBinding>(R.layout.fragment_reklam) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.playstoreBtn.setOnClickListener {

            val url = "https://play.google.com/store/apps/details?id=com.eneserkocak.randevu"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)

        }

        /*binding.linkedinBtn.setOnClickListener {
            val url = "https://linkedin.com/in/enes-erkoçak-061963253"

            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)

        }*/

       binding.youtubeBtn.setOnClickListener {
            val url = "https://www.youtube.com/watch?v=hct3U9nxxdc"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

         binding.politikaBtn.setOnClickListener {
           val url = "https://www.freeprivacypolicy.com/live/327ddcb7-0994-4771-98f6-491c4db81645"
           val i = Intent(Intent.ACTION_VIEW)
           i.data = Uri.parse(url)
           startActivity(i)

       }

        /*binding.politikaUrlBtn.setOnClickListener {
            val url = "https://www.freeprivacypolicy.com/live/327ddcb7-0994-4771-98f6-491c4db81645"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)

        }*/

    }
}