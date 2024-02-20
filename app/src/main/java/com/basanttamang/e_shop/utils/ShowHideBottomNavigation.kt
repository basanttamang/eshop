package com.basanttamang.e_shop.utils

import androidx.fragment.app.Fragment
import com.basanttamang.e_shop.activities.ShoppingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNavigation(){
    val bottomNavigationView = (activity as ShoppingActivity)
        .findViewById<BottomNavigationView>(
        com.basanttamang.e_shop.R.id.bottomNavigation
    )
    bottomNavigationView.visibility = android.view.View.GONE
}
fun Fragment.showBottomNavigation(){
    val bottomNavigationView = (activity as ShoppingActivity)
        .findViewById<BottomNavigationView>(
            com.basanttamang.e_shop.R.id.bottomNavigation
        )
    bottomNavigationView.visibility = android.view.View.VISIBLE
}