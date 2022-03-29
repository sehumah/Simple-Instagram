package com.assignment5.instagram

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class PostAdapter (private val context: Context, private val posts: List<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    // class responsible for laying out the individual items in the layout file
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val ivPostOwnerProfileIcon: ImageView
        private val ivImagePost: ImageView
        private val tvPostOwnerUsername: TextView
        private val tvLikesCounter: TextView
        private val tvCaption: TextView

        // upon initializing this class, find the variables using their IDs
        init {
            ivPostOwnerProfileIcon = itemView.findViewById(R.id.iv_post_owner_profile_icon)
            tvPostOwnerUsername = itemView.findViewById(R.id.tv_post_owner_username)
            ivImagePost = itemView.findViewById(R.id.iv_image_post)
            tvLikesCounter = itemView.findViewById(R.id.tv_likes_counter)
            tvCaption = itemView.findViewById(R.id.et_caption)
        }

        // implement bind method which will associate the data with the correct views in the layout
        fun bind(post: Post) {  // take in a post and set the appropriate views
            // populate text views
            tvPostOwnerUsername.text = post.getUser()?.username
            tvLikesCounter.text = "xxx likes"
            tvCaption.text = post.getCaption()

            // populate image views
            Glide.with(itemView.context).load(post.getImage()?.url).into(ivImagePost)
            Glide.with(itemView.context).load("").into(ivPostOwnerProfileIcon)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
         return posts.size
    }
}

