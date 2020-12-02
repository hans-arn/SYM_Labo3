
package com.example.labo03

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
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
}