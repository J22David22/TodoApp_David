package com.example.projectc4g5

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Pair
import android.util.Patterns
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    lateinit var labelRegistrar: TextView
    lateinit var labelBienvenida: TextView
    lateinit var buttonLogin: Button
    lateinit var imageLogin: ImageView

    lateinit var txtInputUsuario:  TextInputEditText
    lateinit var txtInputPassword:  TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        labelRegistrar = findViewById(R.id.labelRegistrar)
        labelBienvenida= findViewById(R.id.labelBienvenida)
        imageLogin = findViewById(R.id.imageLogin)
        buttonLogin = findViewById(R.id.buttonLogin)

        txtInputUsuario = findViewById(R.id.txtInputUsuario)
        txtInputPassword = findViewById(R.id.txtInputPassword)

        labelRegistrar.setOnClickListener{
            //Toast.makeText(this,"abrir el registro", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, RegisterActivity::class.java)

            val options = ActivityOptions.makeSceneTransitionAnimation(this,
                Pair.create(imageLogin, "logoImageTrans"),
                Pair.create(labelBienvenida, "labelBienvenidaTrans"),
                Pair.create(labelRegistrar, "labelClickable"),
                Pair.create(buttonLogin, "buttonRegisTrans"))

            startActivity(intent, options.toBundle())
        }

        buttonLogin.setOnClickListener{

            validateLogin()

            //val intent=Intent(this,HomeActivity::class.java)
            //startActivity(intent)
        }
    }

    public fun validateLogin(){

        var email:String = txtInputUsuario.text.toString().trim()
        var password:String = txtInputPassword.text.toString().trim()

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            txtInputUsuario.setError(resources.getString(R.string.text_invalid_email))
            return
        }
        else
        {
            txtInputUsuario.setError(null)

            if(password.isEmpty())
            {
                txtInputPassword.setError(resources.getString(R.string.text_empty_field))
                return
            }else{
                if(email=="jd@email.com"){
                    if (password=="123"){
                        val intent=Intent(this,HomeActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this,resources.getString(R.string.text_perfect), Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(this,resources.getString(R.string.text_wrong_password),Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this,resources.getString(R.string.text_wrong_user),Toast.LENGTH_SHORT).show()
                }
            }
        }


    }
}