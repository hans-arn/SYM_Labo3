package com.example.labo03

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.RemoteException
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.labo03.beaconsUtilities.BeaconItem
import org.altbeacon.beacon.BeaconConsumer
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.Region

// Activité responsable des IBeacons sur l'application
class Ibeacons : AppCompatActivity(), BeaconConsumer {

    // Mise en placce des variables de gestion des Beacons
    private lateinit var adapter: BeaconAdapter
    private lateinit var beaconManager : BeaconManager
    // Création de la liste des Beacons afin de pouvoir afficher les Beacons à proximité
    private var beaconItems :MutableList<BeaconItem> = ArrayList()
    // Définition du LAYOUT pour les Beacons
    private val altbeaconLAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"

    // Au moment de la création de l'activité
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ibeacons)

        // Récupération des boutons pour démarrer le scan ou l'arreter
        var buttonStart = findViewById<Button>(R.id.startScan)
        var buttonStop = findViewById<Button>(R.id.stopScan)
        buttonStop.isEnabled = false;

        // Si l'on ne possède pas les permissions
        if (!checkIfPermissions())
            // On les demande à l'utilisateur (besoin d'une manipulation manuelle)
            requestLocationPermission()
        // Récupération de la Recycler View pour l'affichage des Beacons à proximité
        val rView = findViewById<RecyclerView>(R.id.RecyclerView)

        // Mise en place du beaconManager
        beaconManager = BeaconManager.getInstanceForApplication(this)
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(altbeaconLAYOUT))
        beaconManager.bind(this)
        // Scan tous les 5 secondes pour éviter d'en provoquer trop
        beaconManager.foregroundBetweenScanPeriod = 5000
        beaconManager.updateScanPeriods()

        // Mise en place du beaconAdapter
        adapter = BeaconAdapter()
        rView.adapter = adapter
        rView.layoutManager = LinearLayoutManager(this)

        // Gestion des boutons
        buttonStart.setOnClickListener{
            try {
                beaconManager.startRangingBeaconsInRegion(Region("myRangingUniqueId", null, null, null))
                buttonStop.isEnabled = true
                buttonStart.isEnabled = false
            } catch (e: RemoteException) {
            }
        }
        buttonStop.setOnClickListener{
            try {
                beaconManager.stopRangingBeaconsInRegion(Region("myRangingUniqueId", null, null, null))
                buttonStart.isEnabled = true
                buttonStop.isEnabled = false
            } catch (e: RemoteException) {
            }
        }

    }

    override fun onBeaconServiceConnect() {
        // Si un beacon rentre dans la région, celui-ci est détecté
        beaconManager.removeAllRangeNotifiers()
        beaconManager.addRangeNotifier { beacons, region ->
            beaconItems = ArrayList()
            // Dans le cas où un ou plus de Beacons sont détectés on iter sur les Beacons pour créer un objet Beacon par Beacon et afficher ces objets
            // dans l'activité
            if (beacons.isNotEmpty()) {
                // Itération
                val actualB = beacons.iterator().next()
                // Création de l'objet beacon
                val itemB = BeaconItem(actualB.id1.toString(), actualB.id2.toInt(), actualB.id3.toInt(), actualB.rssi.toString(), actualB.distance.toString())
                // Ajout de l'objet beacon à la liste des beacons scannés
                beaconItems.add(itemB)
            }
            // On retourne les Beacons trouvé et on spécifie le changement d'informations
            adapter.submitItems(beaconItems)
            adapter.notifyDataSetChanged()
            // On laisse savoir à l'utilisateur que le scan est terminé (toutes les 5 secondes)
            Toast.makeText(this, "SCAN UPDATED", Toast.LENGTH_SHORT).show()
        }
    }


    // Permissions

    // Vérification si les permissions sont données par le smartphone de l'utilisateur
    // Renvoit vrai ou faux selon si les permissions sont données ou non
    private fun checkIfPermissions(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        return result == PackageManager.PERMISSION_GRANTED
    }

    // Demande d'accès aux permissions pour permettre le fonctionnement de l'application de scan de Beacons
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
    }

    // Validation ou non de la récupération des permissions nécessaires
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}