package com.example.labo03

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.nfc.NfcManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.labo03.Nfc.Nfcbases

class NfcReader : Nfcbases() {
    private lateinit var mTextView      : TextView
    private lateinit var mButton        : Button
    private lateinit var email          : EditText
    private lateinit var password       : EditText
    private lateinit var text           : TextView

    private val credentials = mutableListOf(
            Pair("jerome","1234")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)
        mTextView = findViewById(R.id.textView_explanation)
        mButton = findViewById(R.id.login)
        email = findViewById(R.id.username)
        password = findViewById(R.id.password)
        mButton.setBackgroundColor(Color.GRAY)

        // recupère le contexte nfc du système
        var manager = this.getSystemService(Context.NFC_SERVICE) as NfcManager
        mNfcAdapter = manager.defaultAdapter

        // test si le NFC est activé ou non
        if (!mNfcAdapter.isEnabled) {
            mTextView.setText("NFC is disabled.")
        }

        mButton.setOnClickListener {
            if (credentials.contains(Pair(email.text.toString(), password.text.toString())) and isNfcActivited) {
                intent = Intent(this, NfcSecurity::class.java)
                startActivity(intent)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            val result = handleIntent(intent)
            if (result.equals( "test")) {
                isNfcActivited = true
                mButton.setBackgroundColor(Color.GREEN);
                mButton.isEnabled = true
            }
        };
    }
}