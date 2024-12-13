package com.dicoding.picodiploma.loginwithanimation.view.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.ui.home.HomeFragment
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity
import com.nafis.bottomnavigation.NafisBottomNavigation

class MainActivity : AppCompatActivity() {

    private val idHome = 1
    private val idScan = 2
    private val idResult = 3
    private val idProfile = 4

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigation = findViewById<NafisBottomNavigation>(R.id.nav_view)
        val navController: NavController = findNavController(R.id.nav_host_fragment_activity_main)

        bottomNavigation.add(NafisBottomNavigation.Model(idHome, R.drawable.ic_home_black_24dp))
        bottomNavigation.add(NafisBottomNavigation.Model(idScan, R.drawable.ic_scan))
        bottomNavigation.add(NafisBottomNavigation.Model(idResult, R.drawable.ic_result))
        bottomNavigation.add(NafisBottomNavigation.Model(idProfile, R.drawable.ic_baseline_person_24))

        bottomNavigation.setOnClickMenuListener { model ->
            when (model.id) {
                idHome -> navController.navigate(R.id.navigation_home)
                idScan -> navController.navigate(R.id.navigation_scan)
                idResult -> navController.navigate(R.id.navigation_result)
                idProfile -> navController.navigate(R.id.navigation_profile)
                else -> {}
            }
        }

        val navigateToResult = intent.getBooleanExtra("navigate_to_result", false)
        if (navigateToResult) {
            bottomNavigation.show(idResult)
            navController.navigate(R.id.navigation_result)
        } else (
                bottomNavigation.show(idHome)
        )

        bottomNavigation.setOnReselectListener { model ->
            Toast.makeText(this, "Item ${model.id} is reselected.", Toast.LENGTH_SHORT).show()
        }

        viewModel.userSession.observe(this, Observer { user ->
            if (!user.isLogin) {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

            }
        })
    }
    

    fun updateBottomNavigationToScan() {
        val bottomNavigation = findViewById<NafisBottomNavigation>(R.id.nav_view)
        bottomNavigation.show(idScan)
    }

}