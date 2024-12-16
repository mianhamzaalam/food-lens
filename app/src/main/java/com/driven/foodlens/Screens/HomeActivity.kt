package com.driven.foodlens.Screens

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.driven.foodlens.MainActivity.Companion.CLEAR_USER_DATA_FLAG
import com.driven.foodlens.R
import com.driven.foodlens.databinding.ActivityHomeBinding
import me.ibrahimsn.lib.OnItemReselectedListener
import me.ibrahimsn.lib.OnItemSelectedListener

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private var token: String? = null
    private var firstname: String? = null
    private var lastname: String? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var googlePreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        googlePreferences = getSharedPreferences("GoogleService", MODE_PRIVATE)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        retrieveIntentData()

        // Close activity if token is not found
        if (token.isNullOrEmpty()) {
            handleTokenNotFound()
            return
        }

        // Set token and names
        setTokenAndNames(token!!, firstname ?: "", lastname ?: "")

        // Setup Navigation Controller
        navController = (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment).navController

        // Setup UI components
        setupFloatingActionButton()
        setupBottomNavigation()
    }

    private fun retrieveIntentData() {
        token = intent.getStringExtra("UserToken") ?: intent.getStringExtra("GUserToken")
        firstname = intent.getStringExtra("FirstName") ?: getSharedPreferences("GoogleService", MODE_PRIVATE).getString("GoogleFirstName", null)
        lastname = intent.getStringExtra("LastName") ?: getSharedPreferences("GoogleService", MODE_PRIVATE).getString("GoogleLastName", null)
    }

    private fun handleTokenNotFound() {
        Toast.makeText(this, "Token not found. Please log in again.", Toast.LENGTH_SHORT).show()
        finish() // Close this activity if no token is found
    }

    private fun setupFloatingActionButton() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.button_slide)
        binding.floatingActionButton.startAnimation(animation)
        binding.floatingActionButton.setOnClickListener {
            navController.navigate(R.id.uploadFragment)
        }
    }

    private fun setupBottomNavigation() {
        binding.smoothBottomBar.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelect(pos: Int): Boolean {
                navigateToDestination(pos)
                return true
            }
        }

        binding.smoothBottomBar.onItemReselectedListener = object : OnItemReselectedListener {
            override fun onItemReselect(pos: Int) {
                navigateToDestination(pos)
            }
        }
    }

    private fun navigateToDestination(pos: Int) {
        when (pos) {
            0 -> navController.navigate(R.id.homeFragment)
            1 -> navController.navigate(R.id.recipeFragment)
            2 -> navController.navigate(R.id.bookMarkFragment)
            3 -> navController.navigate(R.id.profileFragment)
        }
    }

    private fun setTokenAndNames(token: String, firstname: String, lastname: String) {
        Log.d("HomeActivity","Token is:$token")
        saveToSharedPreferences("UserToken", token)
        saveToSharedPreferences("UFirstName", firstname)
        saveToSharedPreferences("ULastName", lastname)
        Log.d("HomeActivity", "Token and names saved in SharedPreferences.")
    }

    private fun saveToSharedPreferences(key: String, value: String) {
        getSharedPreferences("MyAppPreferences", MODE_PRIVATE).edit().apply {
            putString(key, value)
            apply()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (sharedPreferences.getBoolean(CLEAR_USER_DATA_FLAG, false)) {
            clearUser()
        }
    }

    private fun clearUser(){
        with(sharedPreferences.edit()) {
            remove("UserToken")
            remove("UFirstName")
            remove("ULastName")
            putBoolean(CLEAR_USER_DATA_FLAG, false)  // Reset the flag
            apply()
            Log.d("HomeAct", "Clearing User")
        }
        with(googlePreferences.edit()) {
            remove("GoogleFirstName")
            remove("GoogleLastName")
            remove("GoogleEmail")
            remove("GooglePicture")
            putBoolean(CLEAR_USER_DATA_FLAG, false)  // Reset the flag
            apply()
            Log.d("HomeAct", "Clearing GUser")
        }
    }
}