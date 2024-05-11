package com.yuch.storyapp.view.detail

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.yuch.storyapp.data.ResultState
import com.yuch.storyapp.databinding.ActivityDetailBinding
import com.yuch.storyapp.view.ViewModelFactory

class DetailActivity : AppCompatActivity() {
    private val viewModel by viewModels<DetailViewModel>{
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(ID)
        if (id != null) {
            Log.d("DetailActivity", "Story ID: $id")
            viewModel.getDetailStory(id)
        }

        viewModel.story.observe(this) { detailState ->
            when (detailState) {
                is ResultState.Loading -> showLoading(true)
                is ResultState.Success -> {
                    val detailResponse = detailState.data
                    val story = detailResponse?.story
                    if (story != null) {
                        binding.apply {
                            tvDetailName.text = story.name
                            tvDetailDesc.text = story.description
                            Glide.with(binding.root).load(story.photoUrl).into(ivDetailStory)
                        }
                    } else {
                        Toast.makeText(this, "Story not found", Toast.LENGTH_SHORT).show()
                    }
                    showLoading(false)
                }
                is ResultState.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Error: ${detailState.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.pbDetail.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
    }
    companion object {
        const val ID = "ID"
    }
}