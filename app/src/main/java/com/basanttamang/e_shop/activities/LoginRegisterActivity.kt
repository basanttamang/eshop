package com.basanttamang.e_shop.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.basanttamang.e_shop.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_register)
    }
}