package com.example.asspos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private val emailPattern = "[_A-Za-z0-9-]+(\\\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\\\.[A-Za-z0-9]+)*(\\\\.[A-Za-z]{2,})"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val logEmail : EditText = findViewById(R.id.EmailLogin)
        val logPass : EditText = findViewById(R.id.loginPassword)
        val logpasslay : TextInputLayout = findViewById(R.id.loginPasslayout)
        val logBtn : Button = findViewById(R.id.loginBtn)
        val logproBar : ProgressBar = findViewById(R.id.LoginProgressbar)

        val registerNow : TextView = findViewById(R.id.txtLoginnow)

        registerNow.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        logBtn.setOnClickListener {
            logproBar.visibility = View.VISIBLE
            logpasslay.isPasswordVisibilityToggleEnabled = true

            val Email = logEmail.text.toString()
            val password = logPass.text.toString()

            if(Email.isEmpty() || password.isEmpty()){
                if(Email.isEmpty()){
                    logEmail.error = "กรุณาใส่อีเมลของคุณ"
                }
                if(password.isEmpty()){
                    logPass.error = "กรุณาใส่รหัสผ่าน"
                    logpasslay.isPasswordVisibilityToggleEnabled = true
                }
                logproBar.visibility = View.GONE
                Toast.makeText(this, "กรุณากรอกข้อมูล", Toast.LENGTH_SHORT).show()
            }else if(Email.matches(emailPattern.toRegex())){
                logproBar.visibility = View.GONE
                logEmail.error = "กรุณากรอกอีเมล"
                Toast.makeText(this, "กรุณากรอกอีเมล", Toast.LENGTH_SHORT).show()
            }else if(password.length < 6){
                logpasslay.isPasswordVisibilityToggleEnabled = true
                logproBar.visibility = View.GONE
                logPass.error = "รหัสผ่านอย่างน้อย 6 ตัวอักษร"
                Toast.makeText(this, "รหัสผ่านอย่างน้อย 6 ตัวอักษร", Toast.LENGTH_SHORT).show()
            }else{
                auth.signInWithEmailAndPassword(Email, password).addOnCompleteListener {
                    if(it.isSuccessful){
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)


                    }else{
                        Toast.makeText(this, "มีบ้างอย่างผิดพลาด กรุณาลองใหม่อีกครั้ง", Toast.LENGTH_SHORT).show()
                        logproBar.visibility = View.GONE
                    }
                }
            }
        }
    }
}