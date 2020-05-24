package com.example.lab3

import android.content.Context
import android.os.Handler
import android.os.Message
import com.example.lab3.fragment.KirLatFragment
import com.example.lab3.message_samples.MessageSample
import java.lang.ref.WeakReference

class TranslationThread(fragment: KirLatFragment) : Handler() {
    private val myClassWeakReference = WeakReference(fragment)
    private lateinit var kirLatTranslator: KirLatTranslator
    private var sharedPreferencesConfig: SharedPreferencesConfig =
        SharedPreferencesConfig(fragment.requireContext())

    override fun handleMessage(msg: Message) {
        val fragment = myClassWeakReference.get()
        if (fragment != null) {
            kirLatTranslator = KirLatTranslator()
            val requestMessage = msg.obj as MessageSample
            var id = requestMessage.id
            id++
            val responseMessageString =
                kirLatTranslator.kirLatTranslation(requestMessage.messageString)
            val responseMessage = MessageSample(id, responseMessageString)
            fragment.sendingMessageToAdapter(responseMessage)
        }
    }
}