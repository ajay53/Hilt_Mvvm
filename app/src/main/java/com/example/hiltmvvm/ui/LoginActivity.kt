package com.example.hiltmvvm.ui

import android.os.Bundle
import android.os.PersistableBundle
import com.example.hiltmvvm.databinding.ActivityLoginBinding
import com.example.hiltmvvm.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity:BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
    }
}