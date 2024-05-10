package com.yuch.storyapp.view.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.yuch.storyapp.data.ResultState
import com.yuch.storyapp.databinding.ActivityRegisterBinding
import com.yuch.storyapp.view.ViewModelFactory
import com.yuch.storyapp.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>(){
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }
    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.edRegisterName.text?.toString()?:""
            val email = binding.edRegisterEmail.text?.toString()?:""
            val password = binding.edRegisterPassword.text?.toString()?:""

            viewModel.register(name, email, password)

            viewModel.registrationResult.observe(this) { result ->
                showLoading(result is ResultState.Loading)

                when (result) {
                    is ResultState.Success -> {
                        showLoading(false)
                        result.data.let { data ->
                            data.message?.let {message -> AlertDialog.Builder(this).apply {
                                setTitle("Yeah!")
                                setMessage("Akun dengan $email sudah jadi nih. Yuk, login dan belajar coding.")
                                setPositiveButton("Lanjut") { _, _ ->
                                    finish()
                                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                                }
                                create()
                                show()
                            }}
                        }
                    }
                    is ResultState.Error -> {
                        showLoading(false)
                        AlertDialog.Builder(this).apply {
                            setTitle("Oops...")
                            setMessage(result.error)
                            setPositiveButton("Coba Lagi") { _, _ ->
                            }
                            create()
                            show()
                        }
                    }
                    is ResultState.Loading -> showLoading(true)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbLogin.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
    }

}