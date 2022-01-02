package com.stone.barcode

import android.content.Context
import android.widget.Toast

class ShowToast {
    companion object{
        public fun long(context: Context,text:String){
            Toast.makeText(context,text,Toast.LENGTH_LONG).show()
        }
        public fun short(context: Context,text:String){
            Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
        }
    }
}