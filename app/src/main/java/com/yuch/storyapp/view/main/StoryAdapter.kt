package com.yuch.storyapp.view.main

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yuch.storyapp.data.response.ListStoryItem
import com.yuch.storyapp.databinding.StoryItemBinding
import com.yuch.storyapp.view.detail.DetailActivity

//@Suppress("UNCHECKED_CAST")
//class StoryAdapter : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {
//    private val list = mutableListOf<ListStoryItem>()
//    private var onItemClickCallback: OnItemClickCallback? = null
//
//    fun setItemClickCallback(onItemClickCallback: OnItemClickCallback) {
//        this.onItemClickCallback = onItemClickCallback
//    }
//
//    fun setList(stories: List<ListStoryItem?>?) {
//        list.clear()
//        list.addAll(stories as List<ListStoryItem>)
//        notifyDataSetChanged()
//    }
//
//    inner class MyViewHolder(private val binding: StoryItemBinding) :
////        RecyclerView.ViewHolder(binding.root) {
////        fun bind(itemName: ListStoryItem) {
////            Glide.with(binding.root)
////                .load(itemName.photoUrl)
////                .into(binding.ivItemStory)
////            binding.tvItemName.text = itemName.name
////            binding.tvItemDesc.text = itemName.description
////            binding.root.setOnClickListener {
////                val intentDetail = Intent(binding.root.context, DetailActivity::class.java)
////                intentDetail.putExtra(DetailActivity.ID, itemName.id)
////                intentDetail.putExtra(DetailActivity.NAME, itemName.name)
////                intentDetail.putExtra(DetailActivity.DESCRIPTION, itemName.description)
////                intentDetail.putExtra(DetailActivity.PICTURE, itemName.photoUrl)
////                binding.root.context.startActivity(intentDetail)
// GABERHASIL BJIRLAH PAKE YANG BAWAH AJA
////            }
////        }
////    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
//        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return StoryViewHolder(binding)
//    }
//
//    override fun getItemCount(): Int = list.size
//
//    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
//        holder.bind(list[position])
//        holder.itemView.setOnClickListener {
//            list.getOrNull(position)?.let { it1 -> onItemClickCallback?.onItemClicked(it1) }
//        }
//    }
//
////    fun clearList() {
////        list.clear()
////        notifyItemRangeRemoved(0, itemCount)
////    }
//
//    interface OnItemClickCallback {
//        fun onItemClicked(data: ListStoryItem)
//    }
//}

class StoryAdapter : RecyclerView.Adapter<StoryAdapter.MyViewHolder>() {
    private var items: List<ListStoryItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = items[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(data: List<ListStoryItem?>?) {
        items = data as List<ListStoryItem>
        notifyDataSetChanged()
    }

    inner class MyViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemName: ListStoryItem) {
            Glide.with(binding.root)
                .load(itemName.photoUrl)
                .into(binding.ivItemStory)
            binding.tvItemName.text = itemName.name
            binding.tvItemDesc.text = itemName.description
            binding.root.setOnClickListener {
                val intentDetail = Intent(binding.root.context, DetailActivity::class.java)
                intentDetail.putExtra(DetailActivity.ID, itemName.id)
                intentDetail.putExtra(DetailActivity.NAME, itemName.name)
                intentDetail.putExtra(DetailActivity.DESCRIPTION, itemName.description)
                intentDetail.putExtra(DetailActivity.PICTURE, itemName.photoUrl)
                binding.root.context.startActivity(intentDetail)
            }
        }
    }
}
