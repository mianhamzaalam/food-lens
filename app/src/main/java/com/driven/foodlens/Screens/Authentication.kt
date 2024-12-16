package com.driven.foodlens.Screens

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.driven.foodlens.databinding.ActivityAuthenticationBinding


class Authentication : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding
    //private lateinit var googleSignin: GoogleSignin
    private lateinit var signInLauncher: ActivityResultLauncher<Intent>
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//      //  googleSignin = GoogleSignin(this)
//
//        // Register the activity result launcher
//        signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == RESULT_OK && result.data != null) {
//                googleSignin.handleSignInResult(result.data) { success: Boolean, errorMessage: String? ->
//                    if (success) {
//                        navigateToHome()
//                    } else {
//                        showToast("Sign-In Failed: ${errorMessage ?: "Unknown error"}")
//                    }
//                }
//            } else {
//                showToast("Google Sign-In canceled")
//            }
//        }
//
//
//        setupUI()
//    }
//
//    private fun setupUI() {
//        binding.googleSignupButton.setOnClickListener {
//            val signInIntent = googleSignin.getSignInIntent()
//            signInLauncher.launch(signInIntent)
//        }
//    }
//
//    private fun navigateToHome() {
//        startActivity(Intent(this, HomeActivity::class.java))
//        finish()
//    }
//
//    private fun showToast(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }
}
