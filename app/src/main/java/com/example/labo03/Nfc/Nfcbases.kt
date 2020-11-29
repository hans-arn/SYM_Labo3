package com.example.labo03.Nfc

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import androidx.appcompat.app.AppCompatActivity
import java.io.UnsupportedEncodingException
import java.util.*
import kotlin.experimental.and

open class Nfcbases : AppCompatActivity() {
    lateinit var mNfcAdapter: NfcAdapter
    var isNfcActivited = false

    override fun onResume() {
        super.onResume()
        setupForegroundDispatch()
    }

    override fun onPause() {
        super.onPause()
        stopForegroundDispatch()
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

    fun handleIntent(intent: Intent): String? {
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
}