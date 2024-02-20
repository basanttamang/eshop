package com.basanttamang.e_shop.fragments.shopping

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.basanttamang.e_shop.R
import com.basanttamang.e_shop.data.Products
import com.basanttamang.e_shop.databinding.FragmentProductsBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


import org.checkerframework.checker.units.qual.C
import java.io.ByteArrayOutputStream
import java.util.UUID

class ProductFragment:Fragment(R.layout.fragment_products) {

    private lateinit var binding: FragmentProductsBinding

    private lateinit var auth: FirebaseAuth

    private var selectedImages = mutableListOf<Uri>()
    private val selectedColor = mutableListOf<Int>()
    private val productStorage = Firebase.storage.reference
    private val firestore = Firebase.firestore



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        auth = FirebaseAuth.getInstance()
        val categoryList = resources.getStringArray(R.array.categoryList)
        val arrayAdapter = activity?.let { ArrayAdapter(it, R.layout.category_list, categoryList) }
        binding.categoryList.setAdapter(arrayAdapter)



        binding.saveBtn.setOnClickListener {
            val productValidation = ValidateInformation()

            if(!productValidation){
                Toast.makeText(requireContext(),"Please check your inputs.", Toast.LENGTH_LONG).show()
            } else{
                saveProduct()
            }


        }

        binding.buttonColorPicker.setOnClickListener {
            showColorDialog()
        }

        val selectImageActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                val intent = result.data

                //multiple Image
                if(intent?.clipData!=null){
                    val count = intent.clipData?.itemCount ?: 0
                    (0 until count).forEach {
                        val imageUri = intent.clipData?.getItemAt(it)?.uri
                        imageUri?.let {
                            selectedImages.add(it)

                        }
                    }
                } else {
                    val imageUri = intent?.data
                    imageUri?.let { selectedImages.add(it) }
                }

                updateImages()

            }

        }

        binding.buttonImagesPicker.setOnClickListener {
            val intent = Intent(ACTION_GET_CONTENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
            intent.type = "image/*"
            selectImageActivityResult.launch(intent)
        }


    }

    private fun updateImages() {
        binding.tvSelectedImages.text = selectedImages.size.toString()
    }

    private fun showColorDialog() {
        val colorPicker = ColorPickerDialog.Builder(requireContext())
            .setTitle("Product Color")
            .setPositiveButton("Select", object : ColorEnvelopeListener{
                override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
                    envelope?.let {
                        selectedColor.add(it.color)
                        updateColors()
                    }
                }
            })
            .setNegativeButton("Cancel"){
                colorPicker, _ ->
                colorPicker.dismiss()
            }
        colorPicker.show()
    }

    private fun updateColors() {
        var colors = ""
        selectedColor.forEach {
            colors = "$colors ${Integer.toHexString(it)}"
        }
        binding.tvSelectedColors.text = colors
    }

    private fun saveProduct() {
        val name = binding.edName.text.toString().trim()
        val category = binding.categoryList.text.toString().trim()
        val price = binding.edPrice.text.toString().trim()
        val brand = binding.edBrand.text.toString().trim()
        val modelYear = binding.madeYear.text.toString().trim()
        val specification = binding.edSpecification.text.toString().trim()
        val imagesByteArrays = getImagesByteArrays()
        val images = mutableListOf<String>()

        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main){
                showLoading()
            }

            try {
                async {
                    imagesByteArrays.forEach {
                        val id = UUID.randomUUID().toString()
                        launch {
                            val imageStorage = productStorage.child("products/images/$id")
                            val result = imageStorage.putBytes(it).await()
                            val downloadUrl = result.storage.downloadUrl.await().toString()
                            images.add(downloadUrl)
                        }
                    }
                }.await()

            } catch (e: java.lang.Exception){
                e.printStackTrace()
                withContext(Dispatchers.Main){
                    hideLoading()
                }

            }

            val product = Products(
                UUID.randomUUID().toString(),
                name,
                category,
                price.toFloat(),
                modelYear.toFloat(),
                brand,
                specification,
                if(selectedColor.isEmpty()) null else selectedColor,
                images
            )
            firestore.collection("products").add(product).addOnSuccessListener {
                hideLoading()
            }.addOnFailureListener {
                hideLoading()
                Log.e("Error", it.message.toString())
            }
        }
    }

    private fun hideLoading() {
        binding.progressbar.visibility = View.INVISIBLE
    }

    private fun showLoading() {
        binding.progressbar.visibility =View.VISIBLE
    }

    private fun getImagesByteArrays(): List<ByteArray> {

        val imagesByteArray = mutableListOf<ByteArray>()
        selectedImages.forEach{
            val stream = ByteArrayOutputStream()
            val imageBap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver,it)
            if(imageBap.compress(Bitmap.CompressFormat.JPEG,100,stream)){
                imagesByteArray.add(stream.toByteArray())
            }
        }
        return imagesByteArray
    }

    private fun ValidateInformation(): Boolean {
        if(binding.edName.text.toString().trim().isEmpty())
                return false
        if(binding.edBrand.text.toString().trim().isEmpty())
            return false
        if(binding.edPrice.text.toString().trim().isEmpty())
            return false
        if(binding.edSpecification.text.toString().trim().isEmpty())
            return false
        if(binding.madeYear.text.toString().trim().isEmpty())
            return false
        if(binding.categoryList.text.toString().trim().isEmpty())
            return false
        if(selectedImages.isEmpty())
            return false

        return  true
    }



}