package com.example.projectc4g5

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.regex.Pattern
import java.util.regex.Pattern.compile
import android.util.Pair as UtilPair

class RegisterActivity : AppCompatActivity() {

    lateinit var labelYaTengoCuenta: TextView
    lateinit var labelRegistro: TextView
    lateinit var buttonRegistrar: MaterialButton
    lateinit var imageRegistrar: ImageView

    lateinit var textInputNombres: TextInputEditText
    lateinit var textInputApellidos: TextInputEditText
    lateinit var textInputIdentidad: TextInputEditText
    lateinit var textInputCorreo: TextInputEditText
    lateinit var textInputTelep: TextInputEditText
    lateinit var textInputPsw1: TextInputEditText
    lateinit var textInputPsw2: TextInputEditText
    lateinit var checkboxPrivacidad: CheckBox


    /* = findViewById(R.id.imageRegister)
    var labelRegistro: TextView = findViewById(R.id.labelRegistro)
    var buttonRegistrar: MaterialButton = findViewById(R.id.buttonRegistrar)*/

    //var labelYaTengoCuenta: TextView = findViewById(R.id.labelYaTengoCuenta)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        //var labelYaTengoCuenta: TextView = findViewById(R.id.labelYaTengoCuenta)

        labelYaTengoCuenta= findViewById(R.id.labelYaTengoCuenta)
        labelRegistro = findViewById(R.id.labelRegistro)
        buttonRegistrar = findViewById(R.id.buttonRegistrar)
        imageRegistrar= findViewById(R.id.imageRegister)

        textInputNombres=findViewById(R.id.textInputNombres)
        textInputApellidos=findViewById(R.id.textInputApellidos)
        textInputIdentidad=findViewById(R.id.textInputIdentidad)
        textInputCorreo=findViewById(R.id.textInputCorreo)
        textInputTelep=findViewById(R.id.textInputTelep)
        textInputPsw1=findViewById(R.id.textInputPsw1)
        textInputPsw2=findViewById(R.id.textInputPsw2)
        checkboxPrivacidad=findViewById(R.id.checkboxPrivacidad)

        labelYaTengoCuenta.setOnClickListener{
            Toast.makeText(this, resources.getString(R.string.text_perfect), Toast.LENGTH_SHORT).show()
            transitionBack()
        }

        buttonRegistrar.setOnClickListener{
            //Toast.makeText(this, "Usuario Registrado", Toast.LENGTH_LONG).show()
            if(!checkboxPrivacidad.isChecked){
                checkboxPrivacidad.setError("")
                Toast.makeText(this,resources.getString(R.string.text_terms_required), Toast.LENGTH_SHORT).show()
            }else{
                checkboxPrivacidad.setError(null)
                validate()
            }

            //transitionBack()
        }
    }

    public fun validate(){
        var nombres: String=textInputNombres.text.toString().trim()
        var apellidos: String=textInputApellidos.text.toString().trim()
        var identidad: String=textInputIdentidad.text.toString().trim()
        var telefono: String=textInputTelep.text.toString().trim()
        var email:String = textInputCorreo.text.toString().trim()
        var password1:String = textInputPsw1.text.toString().trim()
        var password2:String = textInputPsw2.text.toString().trim()

        if(nombres.isEmpty() || Pattern.compile("[\\p{Digit}\\p{Punct}]").matcher(nombres).find())
        {
            textInputNombres.setError(resources.getString(R.string.text_only_text))
            Toast.makeText(this, resources.getString(R.string.text_wrong), Toast.LENGTH_SHORT).show()
            return
        }

        if(apellidos.isEmpty() || Pattern.compile("[\\p{Digit}\\p{Punct}]").matcher(apellidos).find())
        {
            textInputApellidos.setError(resources.getString(R.string.text_only_text))
            Toast.makeText(this, resources.getString(R.string.text_wrong), Toast.LENGTH_SHORT).show()
            return
        }

        if(identidad.isEmpty() || Pattern.compile("[^\\p{Digit}]").matcher(identidad).find())
        {
            textInputIdentidad.setError(resources.getString(R.string.text_only_numbers))
            return
        }

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            textInputCorreo.setError(resources.getString(R.string.text_invalid_email))
            return
        }
        else
        {
            textInputCorreo.setError(null)
        }

        if(telefono.isEmpty() || Pattern.compile("[^\\p{Digit}]").matcher(telefono).find())
        {
            textInputTelep.setError(resources.getString(R.string.text_only_numbers))
            return
        }

        if(password1.isEmpty() || password1.length<8)
        {
            textInputPsw1.setError(resources.getString(R.string.text_password_length))
            return
        }
        else if (!Pattern.compile("[0-9]").matcher(password1).find())
        {
            textInputPsw1.setError(resources.getString(R.string.text_must_have_numbers))
            return
        }
        else if (!Pattern.compile("[A-Z]").matcher(password1).find())
        {
            textInputPsw1.setError(resources.getString(R.string.text_must_have_uppercase))
            return
        }
        else if (!Pattern.compile("[a-z]").matcher(password1).find())
        {
            textInputPsw1.setError(resources.getString(R.string.text_must_have_lowercase))
            return
        }
        else if (!Pattern.compile("[@#$%^&+=.*]").matcher(password1).find())
        {
            textInputPsw1.setError(resources.getString(R.string.text_must_have_characters))
            return
        }
        else{
            textInputPsw1.setError(null)
        }

        if(!password2.equals(password1))
        {
            textInputPsw2.setError(resources.getString(R.string.text_passwords_must_match))
            return
        }else{
            Toast.makeText(this,email+resources.getString(R.string.text_registered),Toast.LENGTH_LONG).show()
        }

        transitionBack()
    }
    
    public fun transitionBack() {
        val intent = Intent(this, LoginActivity::class.java)

        val options = ActivityOptions.makeSceneTransitionAnimation(this,
            UtilPair.create(imageRegistrar, "logoImageTrans"),
            UtilPair.create(labelRegistro, "labelBienvenidaTrans"),
            android.util.Pair.create(labelYaTengoCuenta, "labelClickable"),
            UtilPair.create(buttonRegistrar, "buttonRegisTrans"))

        startActivity(intent, options.toBundle())
    }

}