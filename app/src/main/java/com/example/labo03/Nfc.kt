package com.example.labo03

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.UnsupportedEncodingException
import java.util.*
import kotlin.collections.HashMap
import kotlin.experimental.and


class Nfc : AppCompatActivity() {
    lateinit var mNfcAdapter: NfcAdapter
    private lateinit var mTextView     : TextView

    override fun onResume() {
        super.onResume()
        setupForegroundDispatch()
    }

    override fun onPause() {
        super.onPause()
        stopForegroundDispatch()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)
        mTextView = findViewById(R.id.textView_explanation)

        // définition des crédential
        val hashmap = HashMap<String, String>()
        hashmap["jerome"] = "password"

        // recupère le contexte nfc du système
        var manager = this.getSystemService(Context.NFC_SERVICE) as NfcManager
        mNfcAdapter = manager.defaultAdapter

        // test si le NFC est activé ou non
        if (!mNfcAdapter.isEnabled) {
            mTextView.setText("NFC is disabled.")
        } else {
            mTextView.setText(R.string.explanation)
        }

        // TODO mettre en place la lecture du tag
        // TODO mettre en place la déconnexion

        handleIntent(intent)
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
            if (result != null)
                mTextView.text = "Read content: $result"
            else
                mTextView.text = "Read content: unreadable"
        };
    }

    private fun setupForegroundDispatch() {
        val intent = Intent(this.applicationContext, this.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(this.applicationContext, 0, intent, 0)
        val filters = arrayOfNulls<IntentFilter>(1)
        val techList = arrayOf<Array<String>>()

        // On souhaite être notifié uniquement pour les TAG au format NDEF
        filters[0] = IntentFilter()
        filters[0]!!.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
        filters[0]!!.addCategory(Intent.CATEGORY_DEFAULT)

        try {
            filters[0]!!.addDataType("text/plain")
        } catch (e: IntentFilter.MalformedMimeTypeException) {
        }

        mNfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, techList)
    }

    private fun stopForegroundDispatch() {
        mNfcAdapter.disableForegroundDispatch(this)
    }
}