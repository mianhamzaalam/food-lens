package com.driven.foodlens.Screens
import SignInService
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.driven.foodlens.R
import com.driven.foodlens.Screens.SignUp
import com.driven.foodlens.databinding.ActivitySignInBinding
import com.driven.foodrecipeapp.User.UserRegistration.Model.User
import kotlinx.coroutines.launch

class SignIn : AppCompatActivity() {

    lateinit var binding: ActivitySignInBinding
    private lateinit var firstName: String
    private lateinit var lastName: String
    lateinit var email: String
    private lateinit var confirmPassword: String
    private lateinit var newUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge()

        binding.createAccountButton.setOnClickListener {
            viewsVisibility()
            if (checkValidations()) {
                firstName = binding.firstname.text.toString()
                lastName = binding.lastname.text.toString()
                email = binding.email.text.toString()
                confirmPassword = binding.confirmPassword.text.toString()
                newUser = User(firstName, lastName, email, confirmPassword)

                toggleLoading(true)
                lifecycleScope.launch {
                    handleSignIn(newUser)
                }
            } else {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signup.setOnClickListener {
            val intent = Intent(this@SignIn, SignUp::class.java)
            startActivity(intent)
        }
    }

    private suspend fun handleSignIn(user: User) {
        val signInService = SignInService(this)
        val result = signInService.signIn(user)
        toggleLoading(false)
        result.onSuccess {
            Toast.makeText(this, "User Registration Successful", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, SignUp::class.java))
        }.onFailure {
            Toast.makeText(this, "Registration failed: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkValidations(): Boolean {
        when {
            binding.firstname.text.isNullOrEmpty() -> {
                showToast("First name is required")
                return false
            }
            binding.lastname.text.isNullOrEmpty() -> {
                showToast("Last name is required")
                return false
            }
            binding.email.text.isNullOrEmpty() -> {
                showToast("Email is required")
                return false
            }
            binding.password.text.isNullOrEmpty() -> {
                showToast("Password is required")
                return false
            }
            binding.confirmPassword.text.isNullOrEmpty() -> {
                showToast("Confirm password is required")
                return false
            }
            binding.password.text.toString() != binding.confirmPassword.text.toString() -> {
                showToast("Passwords do not match")
                return false
            }
            else -> return true
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun toggleLoading(isLoading: Boolean) {
        binding.view5.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.progressBar6.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun viewsVisibility() {
        binding.view5.visibility = View.VISIBLE
        binding.progressBar6.visibility = View.VISIBLE
    }

    private fun setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
