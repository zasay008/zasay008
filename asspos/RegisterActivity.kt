package com.example.asspos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.asspos.dataclass.Users
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\\\.+[a-z]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val Rfirstname : EditText = findViewById(R.id.firstName)
        val Rlastname : EditText = findViewById(R.id.LastName)
        val Remail : EditText = findViewById(R.id.EmailRegister)
        val Rpassword : EditText = findViewById(R.id.RegisterPassword)
        val Rcpassword : EditText = findViewById(R.id.RegisterCPassword)
        val passlayout : TextInputLayout = findViewById(R.id.regisPasslayout)
        val Cpasslayout : TextInputLayout = findViewById(R.id.regisCpasslayout)
        val regisBtn : Button = findViewById(R.id.registerBtn)
        val regisPgb : ProgressBar = findViewById(R.id.RegisterProgressbar)

        val loginnowText : TextView = findViewById(R.id.txtRegisternow)

        loginnowText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        regisBtn.setOnClickListener {
            val Firstname = Rfirstname.text.toString()
            val Lastname = Rlastname.text.toString()
            val Email = Remail.text.toString()
            val password = Rpassword.text.toString()
            val Cpassword = Rcpassword.text.toString()

            regisPgb.visibility = View.VISIBLE
            passlayout.isPasswordVisibilityToggleEnabled = true
            Cpasslayout.isPasswordVisibilityToggleEnabled = true

            if(Firstname.isEmpty() || Lastname.isEmpty() || Email.isEmpty() || password.isEmpty() || Cpassword.isEmpty()){
                if(Firstname.isEmpty() || Lastname.isEmpty()){
                    Rfirstname.error = "กรุณาใส่ชื่อและสกุล"
                    Rlastname.error = "กรุณาใส่ชื่อและสกุล"
                }
                if(Email.isEmpty()){
                    Remail.error = "กรุณาใส่อีเมล"
                }
                if(password.isEmpty()){
                    passlayout.isPasswordVisibilityToggleEnabled = true
                    Rpassword.error = "กรุณาใส่รหัสผ่าน"
                }
                if(Cpassword.isEmpty()){
                    Cpasslayout.isPasswordVisibilityToggleEnabled = true
                    Rcpassword.error = "กรุณายืนยันรหัสผ่าน"
                }
                Toast.makeText(this, "กรุณากรอกข้อมูล", Toast.LENGTH_SHORT).show()
                regisPgb.visibility = View.GONE
            }else if(Email.matches(emailPattern.toRegex())){
                regisPgb.visibility = View.GONE
                Remail.error = "อีเมลนี้ถูกใช้งานไปแล้ว"
                Toast.makeText(this, "อีเมลนี้ถูกใช้งานไปแล้ว", Toast.LENGTH_SHORT).show()
            }else if(password.length < 6){
                passlayout.isPasswordVisibilityToggleEnabled = true
                regisPgb.visibility = View.GONE
                Rpassword.error = "รหัสผ่านอย่างน้อย 6 ตัวขึ้นไป"
                Toast.makeText(this, "รหัสผ่านอย่างน้อย 6 ตัวขึ้นไป", Toast.LENGTH_SHORT).show()
            }else if(password != Cpassword){
                Cpasslayout.isPasswordVisibilityToggleEnabled = true
                regisPgb.visibility = View.GONE
                Rcpassword.error = "รหัสผ่านไม่ตรงกัน"
                Toast.makeText(this, "รหัสผ่านไม่ตรงกัน", Toast.LENGTH_SHORT).show()
            }else{
                auth.createUserWithEmailAndPassword(Email, password).addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        val databaseRef = database.reference.child("users").child(auth.currentUser!!.uid)
                        val users : Users = Users(Firstname, Lastname, Email, auth.currentUser!!.uid)

                        databaseRef.setValue(users).addOnCompleteListener {
                            if(it.isSuccessful){
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                            }else{
                                Toast.makeText(this, "ลงทะเบียนไม่สำเร็จ", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "ลงทะเบียนไม่สำเร็จ", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}