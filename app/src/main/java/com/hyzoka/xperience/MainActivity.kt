package com.hyzoka.xperience

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.hyzoka.xperience.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load the binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val menuDrawer = findViewById<ImageView>(R.id.menu_drawer)

        // Set the navigation view's menu
        setNavigationView()

        // Add an icon to the toolbar to open the drawer
        menuDrawer.setOnClickListener {
            // Open the navigation drawer
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun setNavigationView() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        binding.navView.setupWithNavController(navController)

        // Get the navigation view
        binding.navView.setNavigationItemSelectedListener { item ->
            // Handle navigation view item clicks here.
            when (item.itemId) {
                R.id.nav_home -> {
                    // Do something when the home item is clicked
                }
                R.id.nav_random_position -> {
                    // Do something when the random item is clicked
                }
                R.id.nav_all_position -> {
                    Navigation.findNavController(this, R.id.nav_host_fragment_content_main).navigate(R.id.nav_all_position)
                }
            }

            // Close the navigation drawer
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.START)

    }
}