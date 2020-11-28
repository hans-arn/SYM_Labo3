package com.example.labo03

import android.os.Bundle
import com.example.labo03.Nfc.Nfcbases

class NfcSecurity : Nfcbases()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc_security)
    }
}