package com.assignment5.instagram.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.assignment5.instagram.Post
import com.assignment5.instagram.PostAdapter
import com.assignment5.instagram.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery


private const val TAG = "HomeFragment"

open class HomeFragment : Fragment() {

    private lateinit var rvPostsFeed: RecyclerView
    private lateinit var swipeRefreshLayoutContainer: SwipeRefreshLayout
    lateinit var adapter: PostAdapter
    var allPosts: MutableList<Post> = mutableListOf()  // mutable so that I can grab posts from the server and add them to this list

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    // method to instantiate views
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // this is where we setup views, similar to oncreate() for activities
        rvPostsFeed = view.findViewById(R.id.rv_posts_feed)

        // initialize adapter
        adapter = PostAdapter(requireContext(), allPosts)

        // set the adapter for the recycler view
        rvPostsFeed.adapter = adapter

        // set the layout manager
        rvPostsFeed.layoutManager = LinearLayoutManager(requireContext())

        // get reference to the swipe refresh layout
        swipeRefreshLayoutContainer = view.findViewById(R.id.swipe_refresh_layout_container)

        // setup refresh listener to load new data. todo: implement function to load new data later
        swipeRefreshLayoutContainer.setOnRefreshListener {
            // just log something for now
            Log.i(TAG, "swipe refresh detected")
        }

        // Configure refreshing colors for SwipeRefreshLayout
        swipeRefreshLayoutContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        queryForPosts()

        /**
         * Step-by-Step Guide to Populate Any RecyclerView
         * -----------------------------------------------
         * Start populating the recycler view
         *  1. Create a layout for each row in the list of posts retrieved from the server [done]
         *  2. Create a data source for each row (this is the Post class) [done]
         *  3. Create adapter that will bridge data and row layout (PostAdapter.kt) [done]
         *  4. Set adapter on RecyclerView
         *  5. Set layout manager on RecyclerView
         */

    }



    // make a query for all posts from the server (only the last 20 for this assignment)
    open fun queryForPosts() {  // open, so it can be overridden in ProfileFragment
        // specify the class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        // find all post objects in the server
        query.include(Post.KEY_USER)  // return the user object associated with each post

        // return only 20 posts per every request
        query.limit = 20

        // arrange the posts in descending order i.e newer posts will appear first/on the top
        query.addDescendingOrder("createdAt")

        // todo: make modifications below to only return the most recent 20 posts
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
