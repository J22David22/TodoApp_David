package com.example.projectc4g5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {

    private lateinit var appbarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        auth = Firebase.auth

        setSupportActionBar(findViewById(R.id.toolbar_home))

        val drawerLayout: DrawerLayout =findViewById(R.id.draw_lay_home)
        val navView: NavigationView =findViewById(R.id.nav_view)
        val navHostFragment=supportFragmentManager.findFragmentById(R.id.fragment_container_view_home) as NavHostFragment
        val navController=navHostFragment.navController
        val headerView: View = navView.getHeaderView(0)

        appbarConfiguration= AppBarConfiguration(setOf(R.id.nav_home, R.id.nav_about), drawerLayout)

        setupActionBarWithNavController(navController, appbarConfiguration)

        navView.setupWithNavController(navController)

        val useremail: String = auth.currentUser!!.email.toString()
        if(useremail != null){
            Toast.makeText(this ,"On Start:this "+ useremail,Toast.LENGTH_LONG).show()
            useremail.indexOf("@")
            val username=useremail.substring(0,useremail.indexOf("@"))
            Toast.makeText(this ,"Index "+ username,Toast.LENGTH_LONG).show()
            headerView.findViewById<TextView>(R.id.lbl_user_header).text="Welcome \n \n" + username
            headerView.findViewById<TextView>(R.id.lbl_email_header).text=useremail
        }


    }

    override fun onSupportNavigateUp(): Boolean {

        val navHostFragment=supportFragmentManager.findFragmentById(R.id.fragment_container_view_home) as NavHostFragment
        val navController=navHostFragment.navController

        return navController.navigateUp(appbarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem)= when (item.itemId) {
        R.id.action_logout -> {
            Toast.makeText(this, "Loging out", Toast.LENGTH_SHORT).show()
            logout_action()
            var intento = Intent(this@HomeActivity, LoginActivity::class.java)
            startActivity(intento)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }

    }

    private fun logout_action(){
        auth.signOut()
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                // ...
            }
    }
}