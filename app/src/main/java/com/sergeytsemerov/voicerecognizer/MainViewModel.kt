package com.sergeytsemerov.voicerecognizer

import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private lateinit var textToSpeech: TextToSpeech
    private lateinit var startForResult: ActivityResultLauncher<Intent>

    fun init(
        engine: TextToSpeech,
        launcher: ActivityResultLauncher<Intent>
    ) = viewModelScope.launch {
        textToSpeech = engine
        startForResult = launcher
    }

    fun textSpeaker(text: String) = viewModelScope.launch {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, EMPTY_STRING)
    }

    fun showSpeechRecognizer() {
        startForResult.launch(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, LANGUAGE_DEFAULT)
            putExtra(RecognizerIntent.EXTRA_PROMPT, EXTRA_PROMPT_VALUE)
        })
    }

    companion object {
        const val LANGUAGE_DEFAULT = "en-US"
        const val EXTRA_PROMPT_VALUE = "Speak Now"
        const val EMPTY_STRING = ""
    }
}