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
import java.util.*

class NfcReader : Nfcbases() {
    private lateinit var mTextView      : TextView
    private lateinit var mButton        : Button
    private lateinit var email          : EditText
    private lateinit var password       : EditText
    private lateinit var text           : TextView

    // stockage des credentials
    private val credentials = mutableListOf(
            Pair("jerome","1234")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        // initialisation de l'activité
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
            mTextView.text = "NFC is disabled."
        }

        // si l'utilisateur clique sur le bouton et que le tag a été scanné on passe à la prochaine activité
        mButton.setOnClickListener {
            if (credentials.contains(Pair(email.text.toString(), password.text.toString())) and isTagScanned) {
                intent = Intent(this, NfcSecurity::class.java)
                intent.putExtra("scan", lastScan)
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
                isTagScanned = true
                // change la couleur du bouton pour montrer à l'utilisateur le scan du tag
                mButton.setBackgroundColor(Color.GREEN);
                // active le bouton
                mButton.isEnabled = true
                lastScan = Calendar.getInstance().getTime().time
            }
        };
    }
}