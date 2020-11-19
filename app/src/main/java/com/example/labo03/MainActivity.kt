package com.example.labo03

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var iBeacons     : Button
    private lateinit var codeBarres   : Button
    private lateinit var nfc          : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        iBeacons = findViewById(R.id.iBeacons)
        codeBarres = findViewById(R.id.CodesBarres)
        nfc = findViewById(R.id.NFC)


        iBeacons.setOnClickListener{
            intent = Intent(this, Ibeacons::class.java)
            startActivity(intent)
        }

        codeBarres.setOnClickListener{
            intent = Intent(this, codeBarres::class.java)
            startActivity(intent)
        }

        nfc.setOnClickListener{
            intent = Intent(this, Nfc::class.java)
            startActivity(intent)
        }

    }
}