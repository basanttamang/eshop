package com.basanttamang.e_shop.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.basanttamang.e_shop.R
import com.basanttamang.e_shop.activities.ShoppingActivity
import com.basanttamang.e_shop.databinding.FragmentLoginBinding
import com.basanttamang.e_shop.utils.Resource
import com.basanttamang.e_shop.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerTxt.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragement_to_registerFragement)
        }

        binding.apply {
            loginBtn.setOnClickListener {
                val email = inputEmail.text.toString().trim()
                val password = inputPassword.text.toString()

                viewModel.login(email, password)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.login.collect{

                when(it){
                    is Resource.Loading ->{
                        binding.loginBtn.startAnimation()

                    }
                    is Resource.Success ->{
                        binding.loginBtn.revertAnimation()
                        Intent(requireActivity(),ShoppingActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                        binding.loginBtn.revertAnimation()



                    }
                    else -> Unit
                }

            }
        }
    }



}