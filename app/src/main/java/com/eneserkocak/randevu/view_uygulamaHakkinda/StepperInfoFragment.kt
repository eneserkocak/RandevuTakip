package com.eneserkocak.randevu.view_uygulamaHakkinda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.eneserkocak.randevu.R
import com.eneserkocak.randevu.databinding.FragmentStepperBinding
import com.eneserkocak.randevu.databinding.FragmentStepperInfoBinding
import com.eneserkocak.randevu.view_kullaniciRehberi.*
import com.google.android.material.tabs.TabLayoutMediator


class StepperInfoFragment : Fragment() {
    private lateinit var binding: FragmentStepperInfoBinding
    private lateinit var viewPager: ViewPager2

    var currentPage = 0;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TabLayoutMediator(binding.viewPagerTab, binding.viewPager) { tab, position -> }.attach()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View  {
        // Inflate the layout for this fragment
        binding = FragmentStepperInfoBinding.inflate(inflater,null,false)
        viewPager = binding.viewPager
        var activity =  (activity as FragmentActivity).supportFragmentManager
        val pagerAdapter = ViewPagerAdapter(activity)
        binding.viewPager.adapter = pagerAdapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    binding.prev.visibility = View.GONE
                    currentPage = 0
                }
                else if (position == 1) {
                    binding.prev.visibility = View.VISIBLE
                    binding.next.text = getString(R.string.btc_next)
                    currentPage = 1
                }
                else if (position == 2){
                    binding.prev.visibility = View.VISIBLE
                    binding.next.text = getString(R.string.btc_finish)
                    currentPage = 2
                }
                super.onPageSelected(position)
            }
        })
        binding.next.setOnClickListener {
            if (currentPage < 2) {
                currentPage ++
                when (currentPage) {
                    0 -> {
                        viewPager.currentItem = 0

                    }
                    1 -> {
                        viewPager.currentItem = 1
                        binding.prev.visibility = View.VISIBLE
                    }
                    2 -> {
                        viewPager.currentItem = 2
                        binding.next.text = getString(R.string.btc_finish)
                        binding.prev.visibility = View.VISIBLE
                    }
                }
            }
        }

        binding.prev.setOnClickListener {
            if (currentPage > 0) {
                currentPage --
                when (currentPage) {
                    0 -> {
                        viewPager.currentItem = 0
                        it.visibility = View.GONE
                    }
                    1 -> {
                        viewPager.currentItem = 1
                        binding.next.text = getString(R.string.btc_next)
                    }
                    2 -> {
                        viewPager.currentItem = 2
                    }
                }
            }
        }

        return binding.root
    }


    private inner class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> {
                    VeriTabaniFragment()
                }
                1 -> {
                    SesAsistanFragment()
                }

                else -> {
                    HaritaInfoFragment()
                }
            }
        }
    }

}