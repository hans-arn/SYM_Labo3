package com.example.labo03.beaconsUtilities

// Cette classe permet de définir un object Beacon facilement
data class BeaconItem (
     var UUID: String,
     var majeur: Int,
     var mineur: Int,
     var RSSI: String,
     var distance: String
)