package com.careeroinfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.system.Os.remove
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.careeroinfo.networking.HTTP

class Register : AppCompatActivity() {

    lateinit var mapFrame: FrameLayout
    lateinit var info_location: TextView
    lateinit var sp: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        sp = SharedPreference(this)

        mapFrame = findViewById(R.id.mapFrame)
        info_location = findViewById(R.id.info_location)
        var info_action = findViewById<Button>(R.id.info_action)
        var info_subaction = findViewById<TextView>(R.id.info_subaction)

        var info_nic = findViewById<EditText>(R.id.info_nic)
        var info_name = findViewById<EditText>(R.id.info_name)
        var info_email = findViewById<EditText>(R.id.info_email)
        var info_birthday = findViewById<EditText>(R.id.info_birthday)
        var info_address = findViewById<EditText>(R.id.info_address)
        info_location = findViewById<TextView>(R.id.info_location)
        var info_profession = findViewById<EditText>(R.id.info_profession)
        var info_education = findViewById<RadioGroup>(R.id.info_education)
        var info_username = findViewById<EditText>(R.id.info_username)
        var info_password = findViewById<EditText>(R.id.info_password)

        info_action.setOnClickListener(View.OnClickListener {
            val info_educationId = info_education.checkedRadioButtonId
            val info_education_val = resources.getResourceEntryName(info_educationId)
            val http = HTTP(this, this);
            sp.setPreference("info_nic", info_nic.text.toString())
            sp.setPreference("info_name", info_name.text.toString())
            sp.setPreference("info_email", info_email.text.toString())
            sp.setPreference("info_birthday", info_birthday.text.toString())
            sp.setPreference("info_address", info_address.text.toString())
            sp.setPreference("info_location_lat", (info_location.text.toString()).split(", ")[0])
            sp.setPreference("info_location_lng", (info_location.text.toString()).split(", ")[1])
            sp.setPreference("info_profession", info_profession.text.toString())
            sp.setPreference("info_education", (resources.getResourceEntryName(info_educationId).toString()).split("radioButton")[1])
            sp.setPreference("info_username", info_username.text.toString())
            sp.setPreference("info_password", info_password.text.toString())
            http.request("register","{" +
                    "\"info_nic\":\""+ info_nic.text.toString() +"\"," +
                    "\"info_name\":\""+ info_name.text.toString() +"\"," +
                    "\"info_email\":\""+ info_email.text.toString() +"\"," +
                    "\"info_birthday\":\""+ info_birthday.text.toString() +"\"," +
                    "\"info_address\":\""+ info_address.text.toString() +"\"," +
                    "\"info_location_lat\":\""+ (info_location.text.toString()).split(", ")[0] +"\"," +
                    "\"info_location_lng\":\""+ (info_location.text.toString()).split(", ")[1] +"\"," +
                    "\"info_profession\":\""+ info_profession.text.toString() +"\"," +
                    "\"info_education\":\""+ (resources.getResourceEntryName(info_educationId).toString()).split("radioButton")[1] +"\"," +
                    "\"info_username\":\""+ info_username.text.toString() +"\"," +
                    "\"info_password\":\""+ info_password.text.toString() +"\"" +
                    "}")
        })
        info_subaction.setOnClickListener(View.OnClickListener {
            openLogin()
        })
    }

    fun openLocation(view: View){
        mapFrame.visibility = View.VISIBLE
        var transaction:FragmentTransaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mapFrame, MapsFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    fun getLocation(lat: String, lng: String){
        sp.setPreference("info_location_lat", lat)
        sp.setPreference("info_location_lng", lng)
        info_location.text = lat+", "+lng
        mapFrame.visibility = View.GONE
        Toast.makeText(this, "Location configured!", Toast.LENGTH_SHORT).show()
    }

    fun openLogin(){
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

}