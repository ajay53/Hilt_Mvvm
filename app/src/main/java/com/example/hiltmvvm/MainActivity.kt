package com.example.hiltmvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.hiltmvvm.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
//    @Inject
//    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.btnInsert.setOnClickListener(this)
        setContentView(R.layout.activity_main)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.btnInsert.id -> {

            }
        }
    }
}