package com.stone.barcode

import android.os.Bundle
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import androidx.databinding.DataBindingUtil
import com.stone.barcode.databinding.ActivityZxingBinding


class Zxing : AppCompatActivity() {

    private lateinit var binding:ActivityZxingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_zxing)

        binding.scanBtn.setOnClickListener {
            val options = ScanOptions()
            options.captureActivity = PortraitCapture::class.java
            options.setDesiredBarcodeFormats(ScanOptions.ONE_D_CODE_TYPES)
            options.setPrompt("Scan something")
            options.setOrientationLocked(false)
            options.setBeepEnabled(false)
            barcodeLauncher.launch(options)

        }

    }
//    private val barcodeLauncher=
//        registerForActivityResult<ScanOptions, ScanIntentResult>(ScanContract(),
//            ActivityResultCallback<ScanIntentResult> { result: ScanIntentResult ->
//                if (result.getContents() == null) {
//                    Toast.makeText(this@MyActivity, "Cancelled", Toast.LENGTH_LONG).show()
//                } else {
//                    Toast.makeText(
//                        this@MyActivity,
//                        "Scanned: " + result.getContents(),
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            })
//
//    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
//    result -> {
//        if(result.getContents() == null) {
//            Toast.makeText(MyActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(MyActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
//        }
//    });

    private var barcodeLauncher=registerForActivityResult(ScanContract(), ActivityResultCallback { result->
        if (result.contents == null){
            ShowToast.long(this,"Noting")
        }else{
            binding.textContext.text=result.contents.toString()
            ShowToast.long(this,result.contents.toString())
        }
    })

}