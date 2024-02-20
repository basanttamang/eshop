package com.basanttamang.e_shop.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.basanttamang.e_shop.R
import com.basanttamang.e_shop.adapters.HomeViewpagerAdapter
import com.basanttamang.e_shop.databinding.FragmentHomeBinding
import com.basanttamang.e_shop.fragments.categories.BaseCategoryFragment
import com.basanttamang.e_shop.fragments.categories.LaptopFragment
import com.basanttamang.e_shop.fragments.categories.MainCategoryFragment
import com.basanttamang.e_shop.fragments.categories.SmartphoneFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment: Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragments = arrayListOf<Fragment>(
            MainCategoryFragment(),
            BaseCategoryFragment(),
            SmartphoneFragment(),
            LaptopFragment()
        )
        binding.viewpagerHome.isUserInputEnabled = false

        val viewPager2Adapter =
            HomeViewpagerAdapter(categoriesFragments, childFragmentManager, lifecycle)
        binding.viewpagerHome.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout, binding.viewpagerHome){ tab, position ->
            when(position){
                0 -> tab.text = "Main"
                1 -> tab.text = "Smartphone"
                2 -> tab.text = "Laptop"
            }

        }.attach()


    }
}