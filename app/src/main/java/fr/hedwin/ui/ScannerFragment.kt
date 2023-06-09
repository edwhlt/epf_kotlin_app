package fr.hedwin.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager


import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fasterxml.jackson.databind.ObjectMapper
import fr.hedwin.R
import fr.hedwin.tmdb.`object`.DbMovie
import fr.hedwin.tmdb.retrofit.TMDB.FR
import fr.hedwin.tmdb.retrofit.TMDB.SERVICE
import fr.hedwin.ui.dashboard.DashboardFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScannerFragment : Fragment() {

    private lateinit var barcodeView: com.journeyapps.barcodescanner.DecoratedBarcodeView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_scanner, container, false)
        barcodeView = view.findViewById(R.id.scannerView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check and request camera permission if needed
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            startBarcodeScanner()
        }
    }

    override fun onResume() {
        super.onResume()
        barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }

    private fun startBarcodeScanner() {
        barcodeView.decodeContinuous(object : com.journeyapps.barcodescanner.BarcodeCallback {
            override fun barcodeResult(result: com.journeyapps.barcodescanner.BarcodeResult?) {
                // Handle the scanned QR code result
                result?.let {
                    val scannedText = it.text
                    SERVICE.searchMovie(scannedText.toInt(), FR).enqueue(object : Callback<DbMovie?> {
                        override fun onResponse(call: Call<DbMovie?>, response: Response<DbMovie?>) {
                            onPause()
                            val intent = Intent(requireActivity(), MovieActivity::class.java)
                            intent.putExtra("movie", ObjectMapper().writeValueAsString(response.body()))
                            startActivity(intent)
                        }
                        override fun onFailure(call: Call<DbMovie?>, t: Throwable) {
                            t.printStackTrace()
                        }
                    })
                }
            }

            override fun possibleResultPoints(resultPoints: MutableList<com.google.zxing.ResultPoint>?) {}
        })
        barcodeView.setStatusText("")
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }
}