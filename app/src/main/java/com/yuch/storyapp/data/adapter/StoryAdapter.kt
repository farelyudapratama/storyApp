package com.yuch.storyapp.data.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yuch.storyapp.data.response.ListStoryItem
import com.yuch.storyapp.databinding.StoryItemBinding
import com.yuch.storyapp.view.detail.DetailActivity

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(StoryDiffCallback) {
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val storyItem = getItem(position)
        if (storyItem != null) {
            holder.bind(storyItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    inner class MyViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemName: ListStoryItem) {
            binding.apply {
                Glide.with(root)
                    .load(itemName.photoUrl)
                    .into(ivItemStory)
                tvItemName.text = itemName.name
                tvItemDesc.text = itemName.description
                root.setOnClickListener {
                    val intentDetail = Intent(root.context, DetailActivity::class.java)
                    intentDetail.putExtra(DetailActivity.ID, itemName.id)

                    val optionCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        root.context as Activity,
                        Pair(ivItemStory, "photo"),
                        Pair(tvItemName, "name"),
                        Pair(tvItemDesc, "description")
                    )
                    root.context.startActivity(intentDetail, optionCompat.toBundle())
                }
            }
        }
    }
    companion object {
        val StoryDiffCallback = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

        }
    }
}