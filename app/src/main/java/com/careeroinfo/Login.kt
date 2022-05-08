package com.careeroinfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.careeroinfo.networking.HTTP

class Login : AppCompatActivity() {

    lateinit var login_username: EditText
    lateinit var login_password: EditText
    lateinit var login_action: Button
    lateinit var login_subaction: TextView
    lateinit var sp: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        login_username = findViewById(R.id.login_username)
        login_password = findViewById(R.id.login_password)
        login_action = findViewById(R.id.login_action)
        login_subaction = findViewById(R.id.login_subaction)

        sp = SharedPreference(this)

        if(sp.getPreference("ServerHost") == "null"){
            login_password.isEnabled = false
            login_action.isEnabled = false
            login_subaction.isEnabled = false
        }
    }

    fun openRegister(view: View){
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
        finish()
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.i("ServerHost", login_username.text.toString())
            sp.setPreference("ServerHost", login_username.text.toString())
            Toast.makeText(this, "ServerHost Updated!", Toast.LENGTH_SHORT).show()
            login_username.setText("")
            login_password.isEnabled = true
            login_action.isEnabled = true
            login_subaction.isEnabled = true
            true
        } else super.onKeyLongPress(keyCode, event)
    }

    fun login(view: View){
        val http = HTTP(this, this);
        http.request("login","{\"username\":\""+ login_username.text +"\", \"password\":\""+ login_password.text +"\"}")
    }
}