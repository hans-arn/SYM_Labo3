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
        // initialisation de l'activité
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc_security)
        maxSec = findViewById(R.id.MaxSecurity)
        medSec = findViewById(R.id.MediumSecurity)
        minSec = findViewById(R.id.MinSecurity)
        text = findViewById(R.id.chrono)

        // recupère le contexte nfc du système
        var manager = this.getSystemService(Context.NFC_SERVICE) as NfcManager
        mNfcAdapter = manager.defaultAdapter

        // on prépare déjà la déconnexion en cas de fin du compteur
        intent = Intent(this, NfcReader::class.java)

        // débute avec un haut niveau de sécurité et on initialise le compteur à 10 secondes
        setCountDown(10)

        // niveau de sécurité élévé 10 secondes
        maxSec.setOnClickListener{
            if(isTagScanned){
                timer.cancel()
                setCountDown(10)
                resetPermission()
            }
        }

        // niveau de sécurité medium 5 secondes
        medSec.setOnClickListener {
            if(isTagScanned){
                timer.cancel()
                setCountDown(5)
                resetPermission()
            }
        }

        // niveau de sécurité faible 2 secondes
        minSec.setOnClickListener {
            if(isTagScanned){
                timer.cancel()
                setCountDown(2)
                resetPermission()
            }
        }
    }

    /**
     * désactive les boutons et indique qu'on doit rescanner le tag
     */
    private fun resetPermission(){
        isTagScanned = false
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
                isTagScanned = true
                // on active tous les boutons si le tag est scanné
                maxSec.isEnabled = true
                medSec.isEnabled = true
                minSec.isEnabled = true
            }
        };
    }

    /**
     * Prend un temps en paramètre (en secondes)
     * actualise le timer toutes les secondes
     * à la fin change d'activité avec l'intent déclaré plus haut
     */
    private fun setCountDown(timeInSecond : Int){
        var initialTimer = timeInSecond
        timer = object: CountDownTimer((timeInSecond * 1000).toLong(), 1000) {
            // actualise le timer toutes les secondes
            override fun onTick(millisUntilFinished: Long) {
                text.setText("Il reste $initialTimer secondes")
                initialTimer -= 1
            }

            // quand le timer est terminé, on repasse à la page de login
            // et on affiche un toast de déconnexion
            override fun onFinish() {
                Toast.makeText(applicationContext, "déconnexion",
                        Toast.LENGTH_LONG).show();
                startActivity(intent)
            }
        }
        timer.start()
    }
}