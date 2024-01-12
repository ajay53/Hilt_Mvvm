package com.example.hiltmvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.hiltmvvm.R
import com.example.hiltmvvm.viewmodel.MainViewModel
import com.example.hiltmvvm.model.User
import com.example.hiltmvvm.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity(), View.OnClickListener {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController:NavController
    private lateinit var appBarConfiguration : AppBarConfiguration

    //    @Inject
//    https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-factories#kotlin_1
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController)
        initViews()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun initViews() {
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return

        binding.btnInsert.setOnClickListener(this)
        binding.btnGetPost.setOnClickListener(this)

        viewModel.postServiceObject.observe(this) {
            Log.d(TAG, "initViews: postServiceObject: $it")
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.btnInsert.id -> {
                viewModel.insertUser(User(0, "Giraffe", "Long_Neck"))
            }
            binding.btnGetPost.id -> {
                viewModel.setPostIdToFetch(2)
            }
        }
    }
}