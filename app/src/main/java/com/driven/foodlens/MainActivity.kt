package com.driven.foodlens

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.driven.foodlens.Screens.SignUp
import com.driven.foodlens.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding // binding the views with their corresponding classes
    private var progressStatus = 0
    private val handler = Handler() // handler to update the progress bar
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
    }

    private val googlePreferences: SharedPreferences by lazy {
        getSharedPreferences(GOOGLE_PREFS, MODE_PRIVATE)
    }

    companion object {
        const val SHARED_PREFS = "MyAppPreferences"
        const val GOOGLE_PREFS = "GoogleService"
        const val CLEAR_USER_DATA_FLAG = "clearUserFlag"  // New flag
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        windowsFullScreen()
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.getstarted.setOnClickListener {
            // Start the next activity after the progress is complete
            startActivity(Intent(this@MainActivity, SignUp::class.java))
            finish()
        }

        lifecycleScope.launch {
            for (i in 1..100) {
                progressStatus = i
                binding.progressBar.progress = progressStatus
                delay(25)
            }
            binding.progressBar.visibility = View.GONE
            val animation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.button_fadein)
            binding.button.startAnimation(animation).apply {
                binding.button.visibility = View.VISIBLE
            }
        }

    }

    private fun windowsFullScreen(){
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun clearUser(){
        with(sharedPreferences.edit()) {
            remove("UserToken")
            remove("UFirstName")
            remove("ULastName")
            putBoolean(CLEAR_USER_DATA_FLAG, false)  // Reset the flag
            apply()
            Log.d("MainAct", "Clearing User")
        }
        with(googlePreferences.edit()) {
            remove("GoogleFirstName")
            remove("GoogleLastName")
            remove("GoogleEmail")
            remove("GooglePicture")
            putBoolean(CLEAR_USER_DATA_FLAG, false)  // Reset the flag
            apply()
            Log.d("MainAct", "Clearing GUser")
        }
    }
    fun setClearUserFlag() {
        sharedPreferences.edit().putBoolean(CLEAR_USER_DATA_FLAG, true).apply()
        Log.d("MainAct", "User clear flag set.")
    }
}