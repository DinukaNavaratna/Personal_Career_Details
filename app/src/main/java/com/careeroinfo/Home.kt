package com.careeroinfo

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.FragmentTransaction
import com.careeroinfo.networking.File_Upload
import com.careeroinfo.networking.HTTP
import org.w3c.dom.Text
import java.io.File
import android.provider.OpenableColumns
import com.careeroinfo.networking.URIPathHelper


class Home : AppCompatActivity() {

    lateinit var info_nic:EditText
    lateinit var info_name:EditText
    lateinit var info_email:EditText
    lateinit var info_birthday:EditText
    lateinit var info_address:EditText
    lateinit var info_location:TextView
    lateinit var info_profession:EditText
    lateinit var info_education:RadioGroup
    lateinit var info_username:EditText
    lateinit var info_password:EditText
    lateinit var info_action:Button
    lateinit var info_subaction:TextView
    lateinit var mapFrame: FrameLayout
    lateinit var cv_path: String
    lateinit var coverletter_path: String
    lateinit var certificates_path: String
    lateinit var file_cv: Button
    lateinit var file_coverletter: Button
    lateinit var file_certificates: Button
    lateinit var sp: SharedPreference
    lateinit var editButton: ImageButton
    lateinit var home_include_data: View
    lateinit var home_include_qualifications: View
    lateinit var home_include_complaint: View
    lateinit var home_topic: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        sp = SharedPreference(this)

        cv_path = ""
        coverletter_path = ""
        certificates_path = ""

        editButton = findViewById<ImageButton>(R.id.editButton)
        var home_data = findViewById<Button>(R.id.home_data)
        var home_qualifications = findViewById<Button>(R.id.home_qualifications)
        home_include_data = findViewById<View>(R.id.home_include_data)
        home_include_qualifications = findViewById<View>(R.id.home_include_qualifications)
        home_include_complaint = findViewById<View>(R.id.home_include_complaint)
        home_topic = findViewById<TextView>(R.id.home_topic)
        file_cv = findViewById(R.id.file_cv)
        file_coverletter = findViewById(R.id.file_coverletter)
        file_certificates = findViewById(R.id.file_certificates)
        mapFrame = findViewById(R.id.mapFrame)

        info_nic = findViewById<EditText>(R.id.info_nic)
        info_name = findViewById<EditText>(R.id.info_name)
        info_email = findViewById<EditText>(R.id.info_email)
        info_birthday = findViewById<EditText>(R.id.info_birthday)
        info_address = findViewById<EditText>(R.id.info_address)
        info_location = findViewById<TextView>(R.id.info_location)
        info_profession = findViewById<EditText>(R.id.info_profession)
        info_education = findViewById<RadioGroup>(R.id.info_education)
        info_username = findViewById<EditText>(R.id.info_username)
        info_password = findViewById<EditText>(R.id.info_password)
        info_action = findViewById<Button>(R.id.info_action)
        info_subaction = findViewById<TextView>(R.id.info_subaction)

        home_include_qualifications.visibility = View.GONE
        home_include_complaint.visibility = View.GONE
        disableEdit()

        info_nic.setText(sp.getPreference("info_nic"))
        info_name.setText(sp.getPreference("info_name"))
        info_email.setText(sp.getPreference("info_email"))
        info_birthday.setText(sp.getPreference("info_birthday"))
        info_address.setText(sp.getPreference("info_address"))
        info_location.text = sp.getPreference("info_location_lat")+", "+sp.getPreference("info_location_lng")
        info_profession.setText(sp.getPreference("info_profession"))
        info_education.check(resources.getIdentifier("radioButton"+sp.getPreference("info_education"), "id", getPackageName()))
        info_username.setText(sp.getPreference("info_username"))
        info_password.setText(sp.getPreference("info_password"))

        file_cv.text = sp.getPreference("cv")
        file_coverletter.text = sp.getPreference("cl")
        file_certificates.text = sp.getPreference("c")

        home_data.setOnClickListener(View.OnClickListener {
            disableEdit()
            home_topic.text = "My Data"
            home_include_qualifications.visibility = View.GONE
            home_include_data.visibility = View.VISIBLE
            home_include_complaint.visibility = View.GONE
        })
        home_qualifications.setOnClickListener(View.OnClickListener {
            disableEdit()
            home_topic.text = "Qualifications"
            editButton.visibility = View.GONE
            home_include_data.visibility = View.GONE
            home_include_qualifications.visibility = View.VISIBLE
            home_include_complaint.visibility = View.GONE
        })

        editButton.setOnClickListener(View.OnClickListener {
            editButton.visibility = View.GONE
            info_nic.isEnabled = true
            info_name.isEnabled = true
            info_email.isEnabled = true
            info_birthday.isEnabled = true
            info_address.isEnabled = true
            info_location.isEnabled = true
            info_profession.isEnabled = true
            info_education.isEnabled = true
            info_username.isEnabled = true
            info_password.isEnabled = true
            info_action.visibility = View.VISIBLE
            info_action.text = "Update"
            info_subaction.visibility = View.VISIBLE
            info_subaction.text = "Cancel"

            info_action.setOnClickListener(View.OnClickListener {
                disableEdit()
                val info_educationId = info_education.checkedRadioButtonId
                val info_education_val = resources.getResourceEntryName(info_educationId)
                val http = HTTP(this, this);
                http.request("update","{" +
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
                disableEdit()
            })
        })
    }

    fun disableEdit(){
        editButton.visibility = View.VISIBLE
        info_nic.isEnabled = false
        info_name.isEnabled = false
        info_email.isEnabled = false
        info_birthday.isEnabled = false
        info_address.isEnabled = false
        info_location.isEnabled = false
        info_profession.isEnabled = false
        info_education.isEnabled = false
        info_username.isEnabled = false
        info_password.isEnabled = false
        info_action.visibility = View.GONE
        info_subaction.visibility = View.GONE
    }

    fun openLocation(view: View){
        mapFrame.visibility = View.VISIBLE
        var transaction: FragmentTransaction = getSupportFragmentManager().beginTransaction();
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

    fun selectCv(view: View){
        selectFile(10)
    }
    fun selectCoverLetter(view: View){
        selectFile(11)
    }
    fun selectCertificates(view: View){
        selectFile(12)
    }

    fun selectFile(requestCode: Int){
        val intent = Intent()
            .setType("*/*")
            .setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Select a file"), requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val cursor: Cursor? = contentResolver.query(data?.data!!, null, null, null, null)
        val nameIndex: Int = cursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()

        val filePath = URIPathHelper().getPath(this, data?.data!!)
        val fileName: String = cursor.getString(nameIndex)

        if (requestCode == 10 && resultCode == RESULT_OK) {
            cv_path = filePath.toString()
            file_cv.text = fileName
            sp.setPreference("cv", fileName)
        }
        else if (requestCode == 11 && resultCode == RESULT_OK) {
            coverletter_path = filePath.toString()
            file_coverletter.text = fileName
            sp.setPreference("cl", fileName)
        }
        else if (requestCode == 12 && resultCode == RESULT_OK) {
            certificates_path = filePath.toString()
            file_certificates.text = fileName
            sp.setPreference("c", fileName)
        }
    }

    fun logout(view: View){
        sp.setPreference("isLoggedIn", "false")
        Toast.makeText(this, "Bye!", Toast.LENGTH_LONG).show()
        val intent = Intent(this, Splash_Screen::class.java)
        startActivity(intent)
        finish()
    }

    fun complaint(view: View){
        disableEdit()
        editButton.visibility = View.GONE
        home_topic.text = "Complaints"
        home_include_complaint.visibility = View.VISIBLE
        home_include_data.visibility = View.GONE
        home_include_qualifications.visibility = View.GONE
    }
    fun complaint_submit(view: View){
        var complaint_message = findViewById<EditText>(R.id.complaint_message)
        val http = HTTP(this, this);
        http.request("complaint","{" +
                "\"nic\":\""+ sp.getPreference("info_nic") +"\", " +
                "\"name\":\""+ sp.getPreference("info_name") +"\", " +
                "\"email\":\""+ sp.getPreference("info_email") +"\", " +
                "\"message\":\""+ complaint_message.text.toString() +"\"" +
                "}")
    }
    fun complaint_cancel(view: View){
        disableEdit()
        home_topic.text = "My Data"
        home_include_complaint.visibility = View.GONE
        home_include_data.visibility = View.VISIBLE
    }

    fun fileUpload(view: View){
        val fileUpload = File_Upload()
        fileUpload.upload(this, cv_path, coverletter_path, certificates_path, sp.getPreference("info_email"))
    }
}