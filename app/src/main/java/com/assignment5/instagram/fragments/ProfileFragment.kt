package com.assignment5.instagram.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.assignment5.instagram.Post
import com.assignment5.instagram.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser


private const val TAG = "ProfileFragment"

class ProfileFragment : HomeFragment() {

    /*
    // todo: comment onCreateView() out for now until a specific profile layout and an adapter have been implemented for the profile screen
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
    */
    override fun queryForPosts() {  // open, so it can be overridden in ProfileFragment
        // specify the class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        // find all post objects in the server
        query.include(Post.KEY_USER)  // return the user object associated with each post

        // return only 20 posts per every request
        query.limit = 20

        // line below makes it so that only posts made by the currently signed in user are returned for the profile fragment
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())

        // arrange the posts in descending order i.e newer posts will appear first/on the top
        query.addDescendingOrder("createdAt")

        query.findInBackground(object : FindCallback<Post> {
            override fun done(postObjects: MutableList<Post>?, e: ParseException?) {
                if (e == null) {
                    if (postObjects != null) {
                        for (post in postObjects) {
                            Log.i(TAG, "Post: ${post.getCaption()}, username: ${post.getUser()?.username}")
                        }
                        // add all retrieved posts to the list of posts
                        allPosts.addAll(postObjects)

                        // notify the adapter of a dataset change
                        adapter.notifyDataSetChanged()
                    }
                }
                else {
                    Log.e(TAG, "Error fetching posts!")
                }
            }
        })
    }

}
