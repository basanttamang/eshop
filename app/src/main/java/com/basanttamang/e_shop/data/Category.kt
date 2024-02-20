package com.basanttamang.e_shop.data

sealed class Category(val category: String) {
    object Laptop:Category("Laptop")
    object Smartphone: Category("Smartphone")

}