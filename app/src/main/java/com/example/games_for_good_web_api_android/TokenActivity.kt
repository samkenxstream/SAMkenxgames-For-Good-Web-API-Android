package com.example.games_for_good_web_api_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.gson.JsonObject
import org.json.JSONObject

class TokenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_token)

        var tokenData = JSONObject(intent.getStringExtra("token")!!)

        findViewById<TextView>(R.id.token_text).text = tokenData.get("token").toString()


    }
}