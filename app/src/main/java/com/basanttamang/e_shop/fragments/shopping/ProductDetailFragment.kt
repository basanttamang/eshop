package com.basanttamang.e_shop.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.basanttamang.e_shop.adapters.ColorsAdapter
import com.basanttamang.e_shop.adapters.ViewPager2Images
import com.basanttamang.e_shop.databinding.FragmentProductDetailsBinding
import com.basanttamang.e_shop.utils.hideBottomNavigation

class ProductDetailFragment:Fragment() {
    private val args by navArgs<ProductDetailFragmentArgs>()
    private lateinit var binding: FragmentProductDetailsBinding
    private val viewPagerAdapter by lazy { ViewPager2Images() }
    private val colorsAdapter by lazy { ColorsAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hideBottomNavigation()
        binding = FragmentProductDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        binding.imgClose.setOnClickListener {
            findNavController().navigateUp()
        }

        setupColors()
        setupViewPager()
        binding.apply {
            productName.text = product.name
            productPrice.text = "£ ${product.price}"
            productBrand.text = product.brand
            productMadeYear.text = product.modelYear.toString()
            productSpec.text = product.specification

        }
        viewPagerAdapter.differ.submitList(product.images)
        product.colors?.let { colorsAdapter.differ.submitList(it) }

    }

    private fun setupViewPager() {
        binding.apply {
            viewPagerProductImage.adapter = viewPagerAdapter
        }
    }

    private fun setupColors() {
        binding.productColors.apply{
            adapter = colorsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }


}