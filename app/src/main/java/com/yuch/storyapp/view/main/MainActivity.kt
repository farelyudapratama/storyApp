package com.yuch.storyapp.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuch.storyapp.data.ResultState
import com.yuch.storyapp.databinding.ActivityMainBinding
import com.yuch.storyapp.view.ViewModelFactory
import com.yuch.storyapp.view.login.LoginActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>{
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding : ActivityMainBinding
    private lateinit var adapter: StoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        itemDecoration()
        adapter = StoryAdapter()
        binding.rvStoryItem.adapter = adapter
        viewModel.getSession().observe(this) { user ->
            Log.d("token", "onCreate: ${user.token}")
            Log.d("user", "onCreate: ${user.isLogin}, name: ${user.email}")
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            viewModel.storyItem.observe(this) { story ->
                if (story != null) {
                    when (story) {
                        is ResultState.Loading -> {
                            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                            showLoading(true)
                        }

                        is ResultState.Success -> {
                            val storyData = story.data.listStory
                            val storyAdapter = StoryAdapter()
                            storyAdapter.setList(storyData)
                            binding.rvStoryItem.adapter = storyAdapter
                            showLoading(false)
                        }

                        is ResultState.Error -> {
                            showLoading(false)
                        }
                    }
                }
            }
        }

    }

    private fun setupView(){
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
    private fun itemDecoration() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvStoryItem.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStoryItem.addItemDecoration(itemDecoration)
    }
    private fun setupAction() {
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbLogin.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
    }
}