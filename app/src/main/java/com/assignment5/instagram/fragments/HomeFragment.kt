package com.assignment5.instagram.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.assignment5.instagram.R


class HomeFragment : Fragment() {

    private lateinit var rvPostsFeed: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    // method to instantiate views
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // this is where we setup views, similar to oncreate() for activities
        rvPostsFeed = view.findViewById(R.id.rv_posts_feed)

        /**
         * Step-by-Step Guide to Populate Any RecyclerView
         * -----------------------------------------------
         * Start populating the recycler view
         *  1. Create a layout for each row in the list of posts retrieved from the server
         *  2. Create a data source for each row (this is the Post class)
         *  3. Create adapter that will bridge data and row layout
         *  4. Set adapter on RecyclerView
         *  5. Set layout manager on RecyclerView
         */
    }

}
