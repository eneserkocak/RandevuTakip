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
                else if (position == 2) {
                    binding.prev.visibility = View.VISIBLE
                    binding.next.text = getString(R.string.btc_next)
                    currentPage = 2
                }
                else if (position == 3) {
                    binding.prev.visibility = View.VISIBLE
                    binding.next.text = getString(R.string.btc_next)
                    currentPage = 3
                }
                else if (position == 4) {
                    binding.prev.visibility = View.VISIBLE
                    binding.next.text = getString(R.string.btc_next)
                    currentPage = 4
                }
                else if (position == 5){
                    binding.prev.visibility = View.VISIBLE
                    binding.next.text = getString(R.string.btc_finish)
                    currentPage = 5
                }
                super.onPageSelected(position)
            }
        })
        binding.next.setOnClickListener {
            if (currentPage < 5) {
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
                        binding.prev.visibility = View.VISIBLE
                    }
                    3 -> {
                        viewPager.currentItem = 3
                        binding.prev.visibility = View.VISIBLE
                    }
                    4 -> {
                        viewPager.currentItem = 4
                        binding.prev.visibility = View.VISIBLE
                    }
                    5 -> {
                        viewPager.currentItem = 5
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
                        binding.next.text = getString(R.string.btc_next)
                    }
                    3 -> {
                        viewPager.currentItem = 3
                        binding.next.text = getString(R.string.btc_next)
                    }
                    4 -> {
                        viewPager.currentItem = 4
                        binding.next.text = getString(R.string.btc_next)
                    }
                    5 -> {
                        viewPager.currentItem = 5
                    }
                }
            }
        }

        return binding.root
    }


    private inner class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun getItemCount(): Int {
            return 6
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> {
                    VeriTabaniFragment()
                }
                1 -> {
                    VeriAkisiFragment()
                }
                2 -> {
                    SesAsistanFragment()
                }
                3 -> {
                    HaritaInfoFragment()
                }
                4 -> {
                    CallerInfoFragment()
                }
                else -> {
                    ReklamFragment()
                }
            }
        }
    }

}