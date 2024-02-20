package com.basanttamang.e_shop.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Products(
    val id: String,
    val name: String,
    val category: String,
    val price: Float,
    val modelYear: Float,
    val brand: String ,
    val specification: String,
    val colors: List<Int>? = null,
    val images: List<String>
): Parcelable{
    constructor():this("0","","",0f,0f,"","",images = emptyList())
}