package com.example.projectc4g5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var labelRegistrar: TextView = findViewById(R.id.labelRegistrar)

        labelRegistrar.setOnClickListener{
            //Toast.makeText(this,"abrir el registro", Toast.LENGTH_SHORT).show()

            val intento2=Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intento2)
        }
    }
}