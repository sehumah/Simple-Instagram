package com.assignment5.instagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


/**
 * Contains the UI and logic to let the user login to the Parse server
 */

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // set click listener to login button & grab the username and password
        findViewById<Button>(R.id.btn_login_button).setOnClickListener {
            val etUsername = findViewById<EditText>(R.id.et_username).text.toString()
            val etPassword = findViewById<EditText>(R.id.et_password).text.toString()
            loginUser(etUsername, etPassword)
        }
    }

    // make network call to log in the user if they have the right info and already have an account
    private fun loginUser(etUsername: String, etPassword: String) {
        Toast.makeText(this, "$etUsername\n$etPassword", Toast.LENGTH_SHORT).show()  // for now, just toast a message
        // TODO("implement functionality to log the user in to the app")
    }
}
