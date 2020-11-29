package com.example.labo03

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.nfc.NfcManager
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.labo03.Nfc.Nfcbases

class NfcSecurity : Nfcbases()  {
    private lateinit var maxSec : Button
    private lateinit var medSec : Button
    private lateinit var minSec : Button
    private lateinit var text   : TextView
    private lateinit var timer  : CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc_security)
        maxSec = findViewById(R.id.MaxSecurity)
        medSec = findViewById(R.id.MediumSecurity)
        minSec = findViewById(R.id.MinSecurity)
        text = findViewById(R.id.chrono)

        // recupère le contexte nfc du système
        var manager = this.getSystemService(Context.NFC_SERVICE) as NfcManager
        mNfcAdapter = manager.defaultAdapter
        intent = Intent(this, NfcReader::class.java)
        setCountDown(10)

        maxSec.setOnClickListener{
            if(isNfcActivited){
                timer.cancel()
                setCountDown(10)
                resetPermission()
            }
        }

        medSec.setOnClickListener {
            if(isNfcActivited){
                timer.cancel()
                setCountDown(5)
                resetPermission()
            }
        }

        minSec.setOnClickListener {
            if(isNfcActivited){
                timer.cancel()
                setCountDown(2)
                resetPermission()
            }
        }
    }

    private fun resetPermission(){
        isNfcActivited = false
        maxSec.isEnabled = false
        medSec.isEnabled = false
        minSec.isEnabled = false
    }

    @SuppressLint("SetTextI18n")
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            val result = handleIntent(intent)
            if (result.equals( "test")) {
                isNfcActivited = true
                maxSec.isEnabled = true
                medSec.isEnabled = true
                minSec.isEnabled = true
            }
        };
    }

    private fun setCountDown(timeInSecond : Int){
        var initialTimer = timeInSecond
        timer = object: CountDownTimer((timeInSecond * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                text.setText("Il reste $initialTimer secondes")
                initialTimer -= 1
            }

            override fun onFinish() {
                Toast.makeText(applicationContext, "déconnexion",
                        Toast.LENGTH_LONG).show();
                startActivity(intent)
            }
        }
        timer.start()
    }
}