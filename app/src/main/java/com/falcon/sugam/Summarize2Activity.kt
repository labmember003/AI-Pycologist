package com.falcon.sugam

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import org.json.JSONObject
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ui.AppBarConfiguration
import com.example.summarizer.retrofit.ApiUtilities
import com.example.summarizer.retrofit.ChatRequest
import com.falcon.sugam.databinding.ActivitySummarize2Binding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import java.util.*

var url = "http://34.171.182.83/upload"
var url3 = "http://34.171.182.83/follow_up"
var globalidValue = 0
class Summarize2Activity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivitySummarize2Binding
    lateinit var tts: TextToSpeech
    var language : String = ""
    var isAudioEnabled = true
    private val RQ_SPEECH_RC = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivitySummarize2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        language = intent.getStringExtra("language") ?: "English"

        findViewById<ImageView>(R.id.speakerButton).setOnClickListener {
            if (isAudioEnabled) {
                isAudioEnabled = !isAudioEnabled
                findViewById<ImageView>(R.id.speakerButton).setImageResource(R.drawable.mute)

            } else {
                isAudioEnabled = !isAudioEnabled
                findViewById<ImageView>(R.id.speakerButton).setImageResource(R.drawable.speaker)
            }


        }
        binding.micButton.setOnClickListener {
            askSpeechInput()
        }
        binding.btnSend.setOnClickListener {
            val inflater = LayoutInflater.from(this)
            val customView = inflater.inflate(R.layout.message_item, null)
            customView.findViewById<TextView>(R.id.tv_message).text = binding.etMessage.text
            customView.findViewById<TextView>(R.id.tv_bot_message).visibility = View.GONE
            binding.llhehe.addView(customView)
            sendFollowUpRequest(binding.etMessage.text.toString())
        }
        sendBotMessage("Hi, I am Mr Karan Sehgal")
    }

    private fun sendFollowUpRequest(text: String) {
        val id: Int = globalidValue
        url3 = "http://34.171.182.83/follow_up?id=$id&text=$text&lang=$language"
        Fuel.post(url3)
            .responseString { _, response, result ->
                when (result) {
                    is Result.Success -> {
                        val responseBody = result.get()
                        val resultJson = JSONObject(responseBody)
                        val textValue = resultJson.getString("followup")
                        Log.i("meribilli", textValue)
                        Handler(Looper.getMainLooper()).post {
                            sendBotMessage(textValue)
                        }
                    }
                    is Result.Failure -> {
                        val error = result.getException()
                        Log.i("meribilli", error.message.toString())
                        // Handle the error case
                        println("Request failed: ${error.message}")
                    }
                }
            }
    }

    private fun sendBotMessage(textValue: String) {
        val inflater = LayoutInflater.from(this)
        val customView = inflater.inflate(R.layout.message_item, null)
        customView.findViewById<TextView>(R.id.tv_bot_message).text = textValue
        customView.findViewById<TextView>(R.id.tv_message).visibility = View.GONE
        binding.llhehe.addView(customView)
        if (isAudioEnabled) {
            tts = TextToSpeech(this.applicationContext, TextToSpeech.OnInitListener {
                if (it == TextToSpeech.SUCCESS) {
                    tts.language = Locale.US
                    tts.setSpeechRate(1.0f)
                    tts.speak(textValue, TextToSpeech.QUEUE_ADD, null)
                }
            })
        }
    }

    private fun sendUserMessage(textValue: String) {
        val inflater = LayoutInflater.from(this)
        val customView = inflater.inflate(R.layout.message_item, null)
        customView.findViewById<TextView>(R.id.tv_bot_message).visibility = View.GONE
        customView.findViewById<TextView>(R.id.tv_message).text =  textValue
        binding.llhehe.addView(customView)

        val onSuccess = { response: String ->
            sendBotMessage(response)
        }
        val onFailure = { response: String ->
            sendBotMessage(response)
        }
        gptResponse("English", textValue, "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", onSuccess, onFailure)
    }

    private fun askSpeechInput() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Toast.makeText(this, "SAY SOMETHING", Toast.LENGTH_SHORT).show()

        } else {
            val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            i.putExtra(RecognizerIntent.EXTRA_PROMPT, "SAY SOMETHING BRUH")
            startActivityForResult(i, RQ_SPEECH_RC)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RQ_SPEECH_RC && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
//            Toast.makeText(this, result?.get(0).toString(), Toast.LENGTH_SHORT).show()
            sendUserMessage(result?.get(0).toString())
        }
    }

    private fun gptResponse(
        language: String?,
        prompt: String,
        API_KEY: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val requestBody = RequestBody.create(
            "application/json".toMediaType(),
            Gson().toJson(
                ChatRequest(
                    1000,
                    "text-davinci-003",
                    prompt = "Just Act Like a demo Phycologist and answer these questions:  $language: \n$prompt",
                    0.7
                )
            )
        )
        Log.i("qwsdrfghjiklp", API_KEY)
        val contentType = "application/json"
        val authorization = "Bearer $API_KEY"

        // Coroutine scope for making the HTTP request
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiUtilities.getApiInterface().getChat(
                    contentType, authorization, requestBody
                )
                val textResponse = response.choices.first().text
                withContext(Dispatchers.Main) {
                    onSuccess(textResponse)
                }
            } catch (e: Exception) {
                val errorResponse = e.message.toString()
                Log.i("hapyhapyhapy", errorResponse)
                withContext(Dispatchers.Main) {
                    onError(errorResponse)
                }
            }
        }
    }
}