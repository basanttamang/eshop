package com.basanttamang.e_shop.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basanttamang.e_shop.data.Products
import com.basanttamang.e_shop.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class MainCategoryViewModel  @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {


    private val _specialProducts = MutableStateFlow<Resource<List<Products>>>(Resource.Unspecified())
    val specialProducts: StateFlow<Resource<List<Products>>> = _specialProducts

    private val _bestProducts = MutableStateFlow<Resource<List<Products>>>(Resource.Unspecified())
    val bestProducts: StateFlow<Resource<List<Products>>> = _bestProducts

    init {
        fetchSpecialProducts()
        fetchBestProducts()
    }

    private fun fetchBestProducts() {
        viewModelScope.launch {
            _bestProducts.emit(Resource.Loading())

        }
        firestore.collection("products").get().addOnSuccessListener { result ->
            val bestProductList = result.toObjects(Products::class.java)
            viewModelScope.launch {
                _bestProducts.emit(Resource.Success(bestProductList))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _bestProducts.emit(Resource.Error(it.message.toString()))
            }
        }
    }

    fun fetchSpecialProducts(){
        viewModelScope.launch {
            _specialProducts.emit(Resource.Loading())

        }
        firestore.collection("products").
                whereEqualTo("category","Laptop").get().addOnSuccessListener { result ->
                val specialProductList = result.toObjects(Products::class.java)
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Success(specialProductList))
                }
        }.addOnFailureListener {
            viewModelScope.launch {
                _specialProducts.emit(Resource.Error(it.message.toString()))
            }
        }
    }

}