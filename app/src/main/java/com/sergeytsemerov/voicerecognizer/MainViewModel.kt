package com.sergeytsemerov.voicerecognizer

import android.speech.tts.TextToSpeech
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private lateinit var textToSpeech: TextToSpeech

    fun init(
        engine: TextToSpeech
    ) = viewModelScope.launch {
        textToSpeech = engine
    }

    fun textSpeaker(text: String) = viewModelScope.launch {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}