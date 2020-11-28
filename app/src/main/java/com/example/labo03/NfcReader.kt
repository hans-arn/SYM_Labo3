package com.example.labo03

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.labo03.Nfc.Nfcbases
import java.io.UnsupportedEncodingException
import java.util.*
import kotlin.experimental.and


class NfcReader : Nfcbases() {
    private lateinit var mTextView      : TextView
    private lateinit var mButton        : Button
    private lateinit var email: EditText
    private lateinit var password: EditText
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

    private fun handleIntent(intent: Intent): String? {
        val action = intent.action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            val ndefMessage = Ndef.get(tag).cachedNdefMessage
            val records = ndefMessage.records
            for (ndefRecord in records) {
                if (ndefRecord.tnf == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.type, NdefRecord.RTD_TEXT)) {
                    try {
                        val payload = ndefRecord.payload
                        val languageCodeLength: Byte = payload[0] and 51
                        // Get the Text
                        return String(payload, languageCodeLength + 1, payload.size - languageCodeLength - 1)
                    } catch (e: UnsupportedEncodingException) { }
                }
            }
        }
        return null
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