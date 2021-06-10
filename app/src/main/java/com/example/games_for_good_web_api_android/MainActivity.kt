package com.example.games_for_good_web_api_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val log_in = findViewById<Button>(R.id.log_in_button)
        val universal_links = findViewById<Button>(R.id.universal_app_links_button)
        val biometrics = findViewById<Button>(R.id.biometrics_button)

        log_in.setOnClickListener {
            val intent = Intent(this, WebAPIActivity::class.java)
            startActivity(intent)
        }
    }
}