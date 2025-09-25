package com.mohd.lis.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mohd.lis.R
import com.mohd.lis.appBase.AppBaseActivity

class MainActivity : AppBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}