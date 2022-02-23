package com.sergeytsemerov.voicerecognizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.viewModels
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        model.init(textToSpeech)
        with(binding) {
            playButton.setOnClickListener {
                val text = editText.text?.trim().toString()
                model.textSpeaker(text.ifEmpty {
                    "There is NO text!"
                })
            }
        }
    }
}