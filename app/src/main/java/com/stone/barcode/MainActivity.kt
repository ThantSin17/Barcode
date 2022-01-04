package com.stone.barcode

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.isNotEmpty
import androidx.databinding.DataBindingUtil
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.stone.barcode.databinding.ActivityMainBinding
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var detector:BarcodeDetector
    private lateinit var cameraSource:CameraSource
    private val TAG="MainActivity"
    private lateinit var cameraRequest: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        Log.i(TAG,"databind")

        detector=BarcodeDetector.Builder(applicationContext).build()
        Log.i(TAG,"detector")
        cameraSource=CameraSource.Builder(applicationContext,detector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true).build();
        Log.i(TAG,"cameraSource")

        binding.surfaceView.holder.addCallback(surfaceCallBack)
        Log.i(TAG,"surfaceCallback")
        detector.setProcessor(processor)
        Log.i(TAG,"setProcessor")

        cameraRequest =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    ShowToast.long(this, "Access Granded")
                } else {
                    ShowToast.long(this, "Access Denied")
                }

            }






    }
    private val processor=object : Detector.Processor<Barcode>{
        override fun release() {

        }

        override fun receiveDetections(detections : Detector.Detections<Barcode>) {
            Log.i(TAG,"processor")
            if (detections!=null && detections.detectedItems.isNotEmpty()){
                val qrcode=detections.detectedItems
                val code=qrcode.valueAt(0)
                binding.result.text=code.displayValue
            }else{
                binding.result.text="Error"
            }
        }
    }

    private val surfaceCallBack=object:SurfaceHolder.Callback{
        @SuppressLint("MissingPermission")
        override fun surfaceCreated(holder: SurfaceHolder) {
            try {

                Log.i(TAG,"camerasource start")
                cameraSource.start(holder)
            } catch (exception: Exception) {
                ShowToast.long(this@MainActivity,"error call back")
            }

        }

        override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

        }

        override fun surfaceDestroyed(p0: SurfaceHolder) {
            Log.i(TAG,"cameraSource destroy")
            cameraSource.stop()
        }
    }

    override fun onStart() {
        super.onStart()
        //checkCameraPermission()
    }

    private fun checkCameraPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            cameraRequest.launch(
                android.Manifest.permission.CAMERA
            )
        }
    }


}