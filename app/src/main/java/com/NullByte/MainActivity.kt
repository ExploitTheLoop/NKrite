package com.NullByte

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.NullByte.databinding.ActivityMainBinding
import com.topjohnwu.superuser.Shell


class MainActivity : AppCompatActivity(){
    lateinit var bind: ActivityMainBinding
    private val SYSTEM_ALERT_WINDOW_REQUEST_CODE = 1234


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        // Change the color of the status bar
        window.statusBarColor = resources.getColor(R.color.black, theme)

        // Optional: If you want the status bar icons to be light (for a dark background)
        window.decorView.systemUiVisibility = 0 // for dark icons



        // Set up button click listeners
        bind.startBtnView.setOnClickListener {
            // Check if the permission is granted
            if (!Settings.canDrawOverlays(this)) {
                // Request permission
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                intent.data = Uri.parse("package:$packageName")
                startActivityForResult(intent, SYSTEM_ALERT_WINDOW_REQUEST_CODE)
            } else {
                // Permission granted, start the FloatingViewService
                startFloatingViewService()
            }
        }

        bind.stopBtnView.setOnClickListener {
            stopFloatingViewService();
        }

        bind.officialWebsite.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.gg/pCRhEHp874"))
            startActivity(intent)
        }

        bind.communityServer.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/animetonee"))
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SYSTEM_ALERT_WINDOW_REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                startFloatingViewService()
            } else {
                Toast.makeText(this, "Permission required to display overlay", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startFloatingViewService() {
        val intent = Intent(this, FloatingViewService::class.java)
        startService(intent)
    }

    private fun stopFloatingViewService() {
        val intent = Intent(this, FloatingViewService::class.java)
        stopService(intent)
    }


}
