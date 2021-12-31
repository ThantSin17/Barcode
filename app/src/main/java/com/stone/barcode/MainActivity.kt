package com.stone.barcode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import androidx.databinding.DataBindingUtil
import com.stone.barcode.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)


        binding.surfaceView.holder.addCallback(object:SurfaceHolder.Callback{
            override fun surfaceCreated(p0: SurfaceHolder) {
                TODO("Not yet implemented")
            }

            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }

            override fun surfaceDestroyed(p0: SurfaceHolder) {
                TODO("Not yet implemented")
            }

        })

    }
}