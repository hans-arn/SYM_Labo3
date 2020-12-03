package com.example.labo03.Nfc

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
import kotlin.properties.Delegates

/**
 * Classe de base pour l'inscription et désinscription aux event NFC selon les codes donnés dans la
 * donnée
 */
open class Nfcbases : AppCompatActivity() {
    // pour test si lecture de tag est activé sur le téléphone
    lateinit var mNfcAdapter: NfcAdapter
    // pour savoir si le tag a été scanné
    var isTagScanned = false
    var lastScan by Delegates.notNull<Long>()

    /**
     * donné dans la donnée
     */
    override fun onResume() {
        super.onResume()
        setupForegroundDispatch()
    }

    /**
     * donné dans la donnée
     */
    override fun onPause() {
        super.onPause()
        stopForegroundDispatch()
    }

    /**
     * donné dans la donnée
     */
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
        } catch (e: IntentFilter.MalformedMimeTypeException) { }

        mNfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, techList)
    }

    /**
     * donné dans la donnée
     */
    private fun stopForegroundDispatch() {
        mNfcAdapter.disableForegroundDispatch(this)
    }

    /**
     * méthode effectuant la lecture du TAG et retournant la première valeur contenue sous format String
     */
    fun handleIntent(intent: Intent): String? {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            val ndefMessage = Ndef.get(tag).cachedNdefMessage
            val records = ndefMessage.records
            // on prend la première valeur du tableau d'élément (qui devrait normalement contenir "text")
            var ndefRecord = records[0]
            if (ndefRecord.tnf == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.type, NdefRecord.RTD_TEXT)) {
                try {
                    val payload = ndefRecord.payload
                    val languageCodeLength: Byte = payload[0] and 51
                    // Get the Text
                    return String(payload, languageCodeLength + 1, payload.size - languageCodeLength - 1)
                } catch (e: UnsupportedEncodingException) { }
            }
        }
        return null
    }
}