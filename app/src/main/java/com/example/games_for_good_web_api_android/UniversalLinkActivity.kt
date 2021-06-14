package com.example.games_for_good_web_api_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import java.net.URLDecoder

class UniversalLinkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_universal_link)

        if(intent.data != null) {
            println(intent.data!!.encodedQuery+"hello")
            var intentF = intent.data!!.encodedQuery
            var list_str = intentF!!.replace("&", "\n").replace("=", " is ")
            findViewById<TextView>(R.id.universal_link_data).text = list_str
        }
        else
        {
            findViewById<TextView>(R.id.universal_link_data).text = "You did not open through universal link"
        }

    }
}