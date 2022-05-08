package com.careeroinfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast

class Splash_Screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        val sp = SharedPreference(this)

        Handler(Looper.getMainLooper()).postDelayed({
            var isNew = sp.getPreference("isNew")
            if(isNew == "false"){
                var isLoggedIn = sp.getPreference("isLoggedIn")
                if(isLoggedIn == "true"){
                    Toast.makeText(this, "Welcome back "+sp.getPreference("info_username"), Toast.LENGTH_LONG).show()
                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                sp.setPreference("isNew", "false")
                sp.setPreference("ServerHost", "null")
                sp.setPreference("info_location_lat", "7.290572")
                sp.setPreference("info_location_lng", "80.633728")
                sp.setPreference("cv", "Select File")
                sp.setPreference("cl", "Select File")
                sp.setPreference("c", "Select File")
                val intent = Intent(this, Register::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000) // 3000 is the delayed time in milliseconds.

    }
}