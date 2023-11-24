package com.falcon.sugam

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.falcon.sugam.databinding.ActivityMainBinding
import java.util.*


var listOfLanguages = listOf(
    "Hindi",
    "English",
    "Marathi",
    "Tamil",
    "Punjabi",
    "Sanskrit",
    "Bhopuri"
)

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var language = ""

        val adapter5 = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, listOfLanguages)
        binding.countriesAutoCompleteTextView.setAdapter(adapter5)
        binding.countriesAutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            language = listOfLanguages[position]
        }
        binding.nextButton.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            intent.putExtra("language", language)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}