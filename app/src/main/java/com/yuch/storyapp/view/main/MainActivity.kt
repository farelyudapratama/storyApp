package com.yuch.storyapp.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuch.storyapp.R
import com.yuch.storyapp.data.ResultState
import com.yuch.storyapp.databinding.ActivityMainBinding
import com.yuch.storyapp.view.ViewModelFactory
import com.yuch.storyapp.view.addStory.AddStoryActivity
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
        recycleViewSetup()
        playAnimation()
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
                            Toast.makeText(this, "Error: ${story.error}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.storyItem
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
    private fun recycleViewSetup() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvStoryItem.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStoryItem.addItemDecoration(itemDecoration)
    }
    private fun setupAction() {
        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu1 -> {
                viewModel.logout()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun playAnimation() {

    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbMain.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
    }
}