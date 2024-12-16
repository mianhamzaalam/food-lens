package com.driven.foodlens.Screens

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.driven.foodlens.R
import com.driven.foodlens.databinding.ActivitySignUpBinding
import com.driven.foodrecipeapp.User.UserLogin.Model.UserData
import com.driven.foodrecipeapp.User.UserLogin.Model.UserToken
import com.driven.foodrecipeapp.User.UserLogin.Object.UserLogObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        binding.signin.setOnClickListener {
            startActivity(Intent(this,SignIn::class.java))
        }

        binding.ButtonSignUp.setOnClickListener {
            if (isInputValid()) {
                toggleLoading(true)
                binding.loading.startAnimation(fadeInAnimation)

                val email = binding.loginMail.text.toString()
                val password = binding.loginPassword.text.toString()

                Log.d("SignUp", "Email: $email, Password: $password")
                storeUserEmail(email)
                performSignUp(UserData(email, password))
            } else {
                showToast("Please fill in all fields correctly.")
            }
        }
    }

    private fun isInputValid(): Boolean {
        val email = binding.loginMail.text.toString()
        val password = binding.loginPassword.text.toString()

        return when {
            email.isBlank() -> {
                showToast("Email is required")
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showToast("Invalid email format")
                false
            }
            password.isBlank() -> {
                showToast("Password is required")
                false
            }
            password.length < 6 -> {
                showToast("Password must be at least 6 characters")
                false
            }
            else -> true
        }
    }

    private fun storeUserEmail(email: String) {
        sharedPreferences.edit()
            .putString("UserMail", email)
            .apply()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun toggleLoading(isLoading: Boolean) {
        binding.loading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun performSignUp(userData: UserData) {
        lifecycleScope.launch {
            toggleLoading(true)
            try {
                val response = withContext(Dispatchers.IO) {
                    UserLogObject.api.getToken(userData)
                }
                toggleLoading(false)

                if (response.isSuccessful && response.body() != null) {
                    val userToken = response.body()!!
                    val token = userToken.token
                    val firstName = userToken.first_name ?: "User"
                    val lastName = userToken.last_name ?: ""

                    Log.d("SignUp", "Sign-up successful. Token: $token")
                    navigateToHome(token, firstName, lastName)
                } else {
                    Log.e("SignUp", "Sign-up failed. Code: ${response.code()}")
                    showToast("Sign-up failed: ${response.message()}")
                }
            } catch (e: Exception) {
                toggleLoading(false)
                Log.e("SignUp", "Sign-up failed with error: ${e.message}", e)
                showToast("Sign-up failed. Please check your internet connection and try again.")
            }
        }
    }

    private fun navigateToHome(token: String, firstName: String, lastName: String) {
        Intent(this, HomeActivity::class.java).apply {
            putExtra("UserToken", token)
            putExtra("FirstName", firstName)
            putExtra("LastName", lastName)
        }.also {
            startActivity(it)
            finish() // Optional: Finish the current activity to prevent returning to sign-up
        }
    }
}
