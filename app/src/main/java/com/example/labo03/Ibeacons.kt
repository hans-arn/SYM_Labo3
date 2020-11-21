package com.example.labo03

/*
import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
*/

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.RemoteException
import android.util.Log
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


class Ibeacons : AppCompatActivity(), BeaconConsumer {

    private lateinit var adapter: BeaconAdapter
    private lateinit var beaconManager : BeaconManager

    private var beaconItems :MutableList<BeaconItem> = ArrayList()

    private val altbeaconLAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ibeacons)

        if (!checkIfPermissions())
            requestLocationPermission()

        val rView = findViewById<RecyclerView>(R.id.RecyclerView)

        beaconManager = BeaconManager.getInstanceForApplication(this)
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(altbeaconLAYOUT))
        beaconManager.bind(this)

        beaconManager.foregroundBetweenScanPeriod = 5000
        beaconManager.updateScanPeriods()

        adapter = BeaconAdapter()
        rView.adapter = adapter
        rView.layoutManager = LinearLayoutManager(this)
    }

    override fun onBeaconServiceConnect() {
        beaconManager.removeAllRangeNotifiers()
        beaconManager.addRangeNotifier { beacons, region ->
            beaconItems = ArrayList()
            if (beacons.isNotEmpty()) {
                val actualB = beacons.iterator().next()
                val itemB = BeaconItem(actualB.id1.toString(), actualB.id2.toInt(), actualB.id3.toInt(), actualB.rssi.toString(), actualB.distance.toString())
                beaconItems.add(itemB)
                Log.i("BEACON REGION", region.toString())
                Log.i("BEACON", itemB.toString())
                Log.i("BEACON LIST", beaconItems.toString())
            }
            adapter.submitItems(beaconItems)
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "SCAN UPDATED", Toast.LENGTH_SHORT).show()
        }

        try {
            beaconManager.startRangingBeaconsInRegion(Region("myRangingUniqueId", null, null, null))
        } catch (e: RemoteException) {
        }
    }

    // Permissions
    private fun checkIfPermissions(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
    }

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