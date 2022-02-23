package com.sergeytsemerov.voicerecognizer

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sergeytsemerov.voicerecognizer.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val model: MainViewModel by viewModels()
    private val textToSpeech: TextToSpeech by lazy {
        TextToSpeech(this) {
            if (it == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale.UK
            }
        }
    }
    private val launchActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val spokenText =
                    result.data?.let { arrayListOf(it.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)) }
                if (spokenText != null) {
                    binding.editText.setText(spokenText[0].toString().removeSurrounding(PREFIX_BRACE, SUFFIX_BRACE))
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            checkForPermission()
        }
        model.init(textToSpeech, launchActivity)
        with(binding) {
            playButton.setOnClickListener {
                val text = editText.text?.trim().toString()
                model.textSpeaker(text.ifEmpty {
                    EMPTY_TEXT_INPUT
                })
            }
            voiceButton.setOnClickListener {
                model.showSpeechRecognizer()
            }
        }
    }

    private fun checkForPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            RECORD_AUDIO_REQUEST_CODE
        )
    }

    companion object {
        const val RECORD_AUDIO_REQUEST_CODE = 1
        const val EMPTY_TEXT_INPUT = "There is NO text!"
        const val PREFIX_BRACE = "["
        const val SUFFIX_BRACE = "]"
    }
}