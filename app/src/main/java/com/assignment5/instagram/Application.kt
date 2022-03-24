package com.assignment5.instagram

import android.app.Application
import com.parse.Parse
import com.parse.ParseObject


/**
 * This class gets called whenever the application is launched for the first time
 *
 */

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        ParseObject.registerSubclass(Post::class.java)  // register the Post class with the app

        // initialize Parse for the Android app can start talking to the Parse server
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_application_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        )
    }
}
