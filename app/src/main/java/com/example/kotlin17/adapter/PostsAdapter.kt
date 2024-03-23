package com.example.kotlin17.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin17.databinding.PostItemBinding
import com.example.kotlin17.model.Post

class PostsAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        with(holder.binding) {
            textViewTitle.text = posts[position].title
            textViewBody.text = posts[position].body
        }
    }

    override fun getItemCount(): Int = posts.size

    inner class PostViewHolder(val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root)
}
