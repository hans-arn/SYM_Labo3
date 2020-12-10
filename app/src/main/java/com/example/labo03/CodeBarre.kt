
package com.example.labo03

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import java.util.*

class CodeBarre : Activity() {
    lateinit var barcodeView: DecoratedBarcodeView
    private var lastText: String? = null
    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            // Avoid duplicate scans
            if(result.text != null && result.text != lastText) {
                lastText = result.text
                barcodeView.setStatusText(result.text)
                val imageView = findViewById<ImageView>(R.id.barcodePreview)
                imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_barre)
        barcodeView = findViewById(R.id.barcode_scanner)

        // Si l'on ne possède pas les permissions
        if (!checkIfPermissions())
        // On les demande à l'utilisateur (besoin d'une manipulation manuelle)
            requestLocationPermission()

        //List for the available code formats to be scanned
        val codeFormats: List<BarcodeFormat> = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)

        barcodeView.getBarcodeView().decoderFactory = DefaultDecoderFactory(codeFormats)
        barcodeView.initializeFromIntent(intent)
        //scan continiously in the activity and using callback function
        barcodeView.decodeContinuous(callback)

    }

    override fun onResume() {
        super.onResume()
        barcodeView.resume()
    }

    // Permissions

    // Vérification si les permissions sont données par le smartphone de l'utilisateur
    // Renvoit vrai ou faux selon si les permissions sont données ou non
    private fun checkIfPermissions(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        return result == PackageManager.PERMISSION_GRANTED
    }

    // Demande d'accès aux permissions pour permettre le fonctionnement de l'application de scan de Beacons
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
    }

    // Validation ou non de la récupération des permissions nécessaires
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "CAMERA Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "CAMERA Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}