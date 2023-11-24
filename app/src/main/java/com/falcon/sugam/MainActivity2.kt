package com.falcon.sugam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.falcon.sugam.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val language = intent.getStringExtra("language")
        Log.i("Commic", "MainActivity2-"+language)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        val i = Intent(this, Summarize2Activity::class.java)
        startActivity(i)


        binding.buttonSummarize1.setOnClickListener {
            val intent = Intent(this, SummarizeActivity::class.java)
            intent.putExtra("language", language)
            startActivity(intent)
        }
        binding.buttonComify2.setOnClickListener {
            val intent = Intent(this, CommifyActivity::class.java)
            Log.i("Commic", "MainActivitiy- "+language)
            intent.putExtra("language", language)
            startActivity(intent)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main2)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}