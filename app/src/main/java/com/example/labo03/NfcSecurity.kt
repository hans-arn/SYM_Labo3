package com.example.labo03

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.nfc.NfcManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.labo03.Nfc.Nfcbases
import java.util.*

class NfcSecurity : Nfcbases()  {
    private lateinit var maxSec : Button
    private lateinit var medSec : Button
    private lateinit var minSec : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        // initialisation de l'activité
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc_security)
        maxSec = findViewById(R.id.MaxSecurity)
        medSec = findViewById(R.id.MediumSecurity)
        minSec = findViewById(R.id.MinSecurity)
        // on récupère la date du dernier scan de l'application login, par défaut on prend le temps
        // actuel
        lastScan = intent.getLongExtra("scan", Calendar.getInstance().getTime().time)

        // recupère le contexte nfc du système
        val manager = this.getSystemService(Context.NFC_SERVICE) as NfcManager
        mNfcAdapter = manager.defaultAdapter

        // on prépare déjà la déconnexion en cas de fin du compteur
        intent = Intent(this, NfcReader::class.java)

        // niveau de sécurité élévé 10 secondes
        // si l'exigence n'est pas remplie on renvoie au login
        maxSec.setOnClickListener{
            if(!isAllowed(10))
                startActivity(intent)
        }

        // niveau de sécurité medium 5 secondes
        // si plus accès on désactive le bouton
        medSec.setOnClickListener {
            if(!isAllowed(5))
                medSec.isEnabled = false
        }

        // niveau de sécurité faible 2 secondes
        // si plus accès on désactive le bouton
        minSec.setOnClickListener {
            if(!isAllowed(2))
                minSec.isEnabled = false
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            val result = handleIntent(intent)
            if (result.equals("test")) {
                // on active tous les boutons si le tag est scanné et on stocke le temps
                lastScan = Calendar.getInstance().getTime().time
                minSec.isEnabled = true
                maxSec.isEnabled = true
                medSec.isEnabled = true
            }
        };
    }

    /**
     * test si le dernier scan a eut lieu après le temps passer en paramètre
     * si le temps est plus  vieux, on retourne true
     */
    private fun isAllowed(limitTime: Int): Boolean{
        val currentTime : Long = Calendar.getInstance().getTime().time
        val timeInSecond = (currentTime - lastScan)/1000
        if ( timeInSecond < limitTime){
            lastScan = currentTime
            Toast.makeText(applicationContext, "Vous pouvez accéder à ce niveau de sécurité",
                    Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(applicationContext, "Pas d'accès, dernier scan il y a $timeInSecond sec",
                    Toast.LENGTH_LONG).show();
        }
        return timeInSecond < limitTime
    }
}