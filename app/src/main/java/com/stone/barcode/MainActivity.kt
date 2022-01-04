package com.stone.barcode

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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.stone.barcode.databinding.ActivityMainBinding
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var cameraRequest: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        cameraRequest =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    ShowToast.long(this, "Access Granded")
                } else {
                    ShowToast.long(this, "Access Denied")
                }

            }


        binding.surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(p0: SurfaceHolder) {

            }

            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, width: Int, height: Int) {
                cameraPreview(width,height)
            }

            override fun surfaceDestroyed(p0: SurfaceHolder) {

            }

        })

    }

    override fun onStart() {
        super.onStart()
        checkCameraPermission()
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

    private fun cameraPreview(width:Int,height:Int) {

        val barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.QR_CODE or Barcode.DATA_MATRIX)
            .build()

        val imageReader=ImageReader.newInstance(width,height,ImageFormat.JPEG,1)
        imageReader.setOnImageAvailableListener({reader->
            val cameraImage=reader.acquireNextImage()

            val buffer=cameraImage.planes.first().buffer
            val bytes=ByteArray(buffer.capacity())
            buffer.get(bytes)

            val bitmap=BitmapFactory.decodeByteArray(bytes,0,bytes.count(),null)
            val frameToProcess= Frame.Builder().setBitmap(bitmap).build()
            val barcodeResults=barcodeDetector.detect(frameToProcess)
            if (barcodeResults.size() > 0) {
                Log.d("TAG", "Barcode detected!")
               ShowToast.long(this,"Barcode detected!!!!!!!!!!!!!!!!!!!!!")
            } else {
                Log.d("TAG", "No barcode found")
            }
            cameraImage.close()

        },Handler{true})

        try {

            checkCameraPermission()
            val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
            cameraManager.cameraIdList.find {
                val characteristics = cameraManager.getCameraCharacteristics(it)
                val cameraDirection = characteristics.get(CameraCharacteristics.LENS_FACING)
                return@find cameraDirection != null && cameraDirection == CameraCharacteristics.LENS_FACING_BACK
            }?.let {

                val cameraStateCallback = object : CameraDevice.StateCallback() {
                    override fun onOpened(camera: CameraDevice) {
                        val captureStateCallback = object : CameraCaptureSession.StateCallback() {
                            override fun onConfigured(session: CameraCaptureSession) {
                                val builder =
                                    camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                                builder.addTarget(binding.surfaceView.holder.surface)
                                session.setRepeatingRequest(builder.build(), null, null)
                            }

                            override fun onConfigureFailed(p0: CameraCaptureSession) {
                                Log.d("MainActivity", "onConfigureFailed")
                            }

                        }
                        camera.createCaptureSession(
                            listOf(binding.surfaceView.holder.surface),
                            captureStateCallback,
                            Handler { true }
                        )
                    }


                    override fun onDisconnected(p0: CameraDevice) {
                        Log.d("MainActivity", "onDisconnected")
                    }

                    override fun onError(p0: CameraDevice, p1: Int) {
                        Log.d("MainActivity", "onError")
                    }

                }

                cameraManager.openCamera(it, cameraStateCallback, Handler { true })
                return
            }

        } catch (e: CameraAccessException) {

            Log.e("MainActivity", e.message!!)
        } catch (e: SecurityException) {

            Log.e("MainActivity", e.message!!)
        }
    }
}