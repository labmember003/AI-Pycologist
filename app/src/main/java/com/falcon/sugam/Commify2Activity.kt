package com.falcon.sugam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.falcon.sugam.databinding.ActivityCommify2Binding
import com.falcon.sugam.databinding.ActivityCommifyBinding

class Commify2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityCommify2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommify2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        val arrayListData: ArrayList<String> = intent.getStringArrayListExtra("data") as ArrayList<String>
//        Toast.makeText(this, arrayListData[0], Toast.LENGTH_SHORT).show()

        binding.chatRCV.adapter = ChatRcvAdapter(arrayListData)
        binding.chatRCV.layoutManager = LinearLayoutManager(this)
    }
}