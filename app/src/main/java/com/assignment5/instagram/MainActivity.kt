package com.assignment5.instagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast


/**
 * Let a user create a post by taking a photo with their camera
 */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    // inflate the resource file to be used and return true to show the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            // navigate to login/signup screen

            // todo: navigating to login activity doesn't seem to be working, figure it out later
            // val intent = Intent(this@MainActivity, LoginActivity::class.java)
            // startActivity(intent)

            // just implement a toast for now
            Toast.makeText(this, "Logout button clicked!", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}
