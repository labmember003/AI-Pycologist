package com.falcon.sugam

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.navigation.ui.AppBarConfiguration
import com.falcon.sugam.databinding.ActivitySummarizeBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.ByteArrayOutputStream

private lateinit var appBarConfiguration: AppBarConfiguration
private lateinit var binding: ActivitySummarizeBinding


class SummarizeActivity : AppCompatActivity() {
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private val REQUEST_IMAGE_CAPTURE=1

    private var imageBitmap: Bitmap? =null
    companion object {
        // Define the pic id
        private const val pic_id = 123
    }
    var language: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        language = intent.getStringExtra("language").toString()
        binding = ActivitySummarizeBinding.inflate(layoutInflater)
        binding.scanButton.setOnClickListener {
            takeImage()
        }
        setContentView(binding.root)
    }

    private fun takeImage(){

        val intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(intent,REQUEST_IMAGE_CAPTURE)
        }
        catch (e:Exception){

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==REQUEST_IMAGE_CAPTURE && resultCode== RESULT_OK){
            val extras: Bundle? = data?.extras
            imageBitmap= extras?.get("data") as Bitmap
            if (imageBitmap!=null) {
//                binding.imageView.setImageBitmap(imageBitmap)
            }
        }
        processImage()
    }

    private fun processImage(){
        if (imageBitmap!=null) {
            val image = imageBitmap?.let {
                InputImage.fromBitmap(it, 0)
            }
            image?.let {
                recognizer.process(it)
                    .addOnSuccessListener { visionText ->
//                        Toast.makeText(this, visionText.text, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, Summarize2Activity::class.java)
                        intent.putExtra("message", visionText.text)
                        intent.putExtra("language", language)
                        startActivity(intent)
//                        binding.textView.text = visionText.text
                    }
                    .addOnFailureListener { e ->

                    }
            }
        }
        else{
            Toast.makeText(this, "Please select photo", Toast.LENGTH_SHORT).show()
        }
    }
}