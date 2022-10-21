package com.example.projectc4g5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Animaciones

        var animacion1:Animation = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_arriba)
        var animacion2:Animation = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_abajo)

        var textViewHechoPor:TextView = findViewById(R.id.textViewHechoPor)
        var textViewTeam9:TextView = findViewById(R.id.textViewTeam9)
        var logoImgView:ImageView=findViewById(R.id.logoImgView)

        textViewHechoPor.setAnimation(animacion2)
        textViewTeam9.setAnimation(animacion2)
        logoImgView.setAnimation(animacion1)

        // Cambiar a la página de inicio de sesión luego de 4 segundos

        Handler(Looper.getMainLooper()).postDelayed({
            val intento=Intent(this,LoginActivity::class.java)
            startActivity(intento)
        }, 3000)

    }
}