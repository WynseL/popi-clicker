package com.wynsel.player

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.wynsel.player.utils.Preference

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Preference.newInstance(this)

//        val myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
//        startActivity(myIntent)

        Intent(this, OverlayFunctionService::class.java).also {
            startService(it)
            this@MainActivity.finish()
        }
    }
}