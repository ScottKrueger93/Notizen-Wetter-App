package com.example.abschlussprojektscott.data

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.abschlussprojektscott.R
import com.example.abschlussprojektscott.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            Boolean
            if (isGranted) {
                Log.i("PERMISSION", "GRANTED")
            } else {
                Log.i("PERMISSION", "GRANTED")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ändert die Farbe der StatusBar auf Schwarz
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //navHost wird angelegt
        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        //Bottom Navigation Bar wird gebindet
        binding.bottomNavigation.setupWithNavController(navHost.navController)

        //Bottom Naviagtion Bar wird ausgeblendet wenn auf bestimmte layouts navigiert wird
        navHost.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.taskDetailFragment -> binding.bottomNavigation.visibility = View.GONE
                R.id.taskEditFragment -> binding.bottomNavigation.visibility = View.GONE
                else -> binding.bottomNavigation.visibility = View.VISIBLE
            }
        }

        // Funktion zum zurrück navigieren über die Seite des Telefons wird erstellt
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding.fragmentContainerView.findNavController().navigateUp()
            }
        })

        checkLocationPermission()

    }

    fun checkLocationPermission(): Boolean {
        return when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                return true
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                return false
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                return false
            }
        }
    }
}