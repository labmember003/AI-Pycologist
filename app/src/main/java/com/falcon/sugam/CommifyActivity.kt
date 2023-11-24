package com.falcon.sugam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.ui.AppBarConfiguration
import com.falcon.sugam.databinding.ActivityCommifyBinding
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

private var url2 = "http://34.171.182.83/comikify"

class CommifyActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityCommifyBinding
    var langauge : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        langauge = intent.getStringExtra("language").toString()
        Log.i("Commic", langauge)
        binding = ActivityCommifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.submitButton.setOnClickListener {
            url2 = "http://34.171.182.83/comikify" + "?topic=" + binding.yourNoteET.text.toString() + "&lang=$langauge"
            getJson()
        }
    }

    private fun getJson() {
        Fuel.get(url2)
            .responseString { _, response, result ->
                when (result) {
                    is Result.Success -> {

                        val responseBody = result.get()
                        val resultJson = JSONObject(responseBody)
                        val resultValue = resultJson.getString("result")
                        val data: List<String> = convertDataToList(resultValue)
                        val arrayListData: ArrayList<String> = ArrayList(data)

                        val intent = Intent(this, Commify2Activity::class.java)
                        intent.putStringArrayListExtra("data", arrayListData)
                        Log.i("Commic", arrayListData.toString())
                        startActivity(intent)
                    }
                    is Result.Failure -> {
                        val error = result.getException()
                        Log.i("Commic", error.message.toString())
                    }
                }
            }
    }
    private fun convertDataToList(data: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(data, listType)
    }
}