package com.example.projectc4g5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var appbarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(findViewById(R.id.toolbar_home))

        val drawerLayout: DrawerLayout =findViewById(R.id.draw_lay_home)
        val navView: NavigationView =findViewById(R.id.nav_view)
        val navHostFragment=supportFragmentManager.findFragmentById(R.id.fragment_container_view_home) as NavHostFragment
        val navController=navHostFragment.navController

        appbarConfiguration= AppBarConfiguration(setOf(R.id.nav_home, R.id.nav_about), drawerLayout)

        setupActionBarWithNavController(navController, appbarConfiguration)

        navView.setupWithNavController(navController)




    }

    override fun onSupportNavigateUp(): Boolean {

        val navHostFragment=supportFragmentManager.findFragmentById(R.id.fragment_container_view_home) as NavHostFragment
        val navController=navHostFragment.navController

        return navController.navigateUp(appbarConfiguration) || super.onSupportNavigateUp()
    }
}