package com.assignment5.instagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.ParseObject


/**
 * Contains the UI and logic to let the user login to the Parse server
 */

private const val TAG = "LoginActivity"

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

        /**
         * Testing initial connection
         */
        // create a ParseObject to test the connection
        val parseObject: ParseObject = ParseObject("TestConnectionClass")
        parseObject.put("message", "Test message from Simple-Instagram Android app. Parse is now connected!")
        parseObject.saveInBackground {
            if (it != null) {
                it.localizedMessage?.let { message -> Log.e(TAG, message) }
            } else {
                Log.d(TAG, "Object saved!")
            }
        }
    }


    // make network call to log in the user if they have the right info and already have an account
    private fun loginUser(etUsername: String, etPassword: String) {
        Toast.makeText(this, "$etUsername\n$etPassword", Toast.LENGTH_SHORT).show()  // for now, just toast a message
        // TODO("implement functionality to log the user in to the app")
    }
}
