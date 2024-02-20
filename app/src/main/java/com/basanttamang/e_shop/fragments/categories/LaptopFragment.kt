package com.basanttamang.e_shop.fragments.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.basanttamang.e_shop.data.Category
import com.basanttamang.e_shop.utils.Resource
import com.basanttamang.e_shop.viewmodels.CategoryViewModel
import com.basanttamang.e_shop.viewmodels.factory.BaseCategoryViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
@AndroidEntryPoint
class LaptopFragment: BaseCategoryFragment() {

    @Inject
    lateinit var firestore: FirebaseFirestore

    val viewModel by viewModels<CategoryViewModel> {
        BaseCategoryViewModelFactory(firestore,Category.Laptop)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.bestProducts.collectLatest {
                when(it) {

                    is Resource.Loading -> {

                    }
                    is Resource.Success ->{
                        offerAdapter.differ.submitList(it.data)
                    }
                    is Resource.Error ->{
                        Snackbar.make(requireView(),it.message.toString(),Snackbar.LENGTH_LONG).show()

                    }
                    else -> Unit

                }
            }
        }
    }

}