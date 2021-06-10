package com.example.games_for_good_web_api_android

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit


class WebAPIActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_apiactivity)
        val emailText = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val passwordText = findViewById<EditText>(R.id.editTextTextPassword)

        val login_button = findViewById<Button>(R.id.log_in)
        val viewmodel = ViewModelProvider(this).get(WebAPIViewmodel::class.java)

        login_button.setOnClickListener {
            viewmodel.postLogin(emailText.text.toString(), passwordText.text.toString(), object: CompletionHandler<Any>{
                override fun onResult(result: Any) {
                    val intent = Intent(applicationContext, TokenActivity::class.java)
                    intent.putExtra("token", result.toString())
                    startActivity(intent)
                }

                override fun onError(e: String) {
                    createToast()
                }
            })
        }
    }

    fun createToast()
    {
        Looper.prepare()
        Toast.makeText(this, "Log In Error Occured!", Toast.LENGTH_SHORT).show()
        Looper.loop()
    }

}