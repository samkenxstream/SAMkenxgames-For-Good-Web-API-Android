package com.example.games_for_good_web_api_android

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.security.KeyChain
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class BiometricsActivity : AppCompatActivity() {
    val PROVIDER =  "AndroidKeyStore"
    val ALIAS = "keystoreAlias"
    lateinit var pair: Pair<ByteArray, ByteArray>
    var hasValue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biometrics)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            createDialog()


            val retrieveButton = findViewById<Button>(R.id.retrieve_encrypt_button)
            val setButton = findViewById<Button>(R.id.set_encrypt_button)
            val clearEncryptedButton  = findViewById<Button>(R.id.clear_encrypt_button)
            retrieveButton.setOnClickListener {
                if(hasValue) {
                    val biometricPrompt = BiometricPrompt(this, ContextCompat.getMainExecutor(this), getCallbackAuth())
                    biometricPrompt.authenticate(getBiometricInfo())
                }
                else
                    Toast.makeText(this, "There is no value in your keychain!",Toast.LENGTH_SHORT).show()
            }
            setButton.setOnClickListener {
                createDialog()
                Toast.makeText(this,"Setting data", Toast.LENGTH_SHORT).show()
            }
            clearEncryptedButton.setOnClickListener {
                encrypt("")
                hasValue = false
                Toast.makeText(this,"Clearing data",Toast.LENGTH_SHORT).show()
            }



        }


        else {
            TODO("VERSION.SDK_INT < M")
        }
    }
    private fun getBiometricInfo():BiometricPrompt.PromptInfo{
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("This is title")
            .setSubtitle("This is subtitle")
            .setDescription("this is descriotion")
            .setNegativeButtonText("Cancel")
            .build()
        return promptInfo
    }

    private fun getCallbackAuth():BiometricPrompt.AuthenticationCallback{
        val callback = object: BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                showData()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                showError(errorCode, errString)
            }
        }
        return callback
    }

    private fun showError(errorCode: Int, errString: CharSequence) {
        Toast.makeText(this,"$errorCode : $errString",Toast.LENGTH_SHORT).show()

    }

    private fun showData() {
        val str = decrypt(pair.first, pair.second)
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setUpKeyGenerator(){
        val keyGenerator  = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, PROVIDER)
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    private fun getKey(): SecretKey {
        val  keystore = KeyStore.getInstance(PROVIDER)
        keystore.load(null)

        val secretKeyEntry = keystore.getEntry(ALIAS,null) as KeyStore.SecretKeyEntry
        return secretKeyEntry.secretKey
    }

    private fun encrypt(data: String):Pair<ByteArray, ByteArray>{
        hasValue = true
        val cipher = Cipher.getInstance("AES/CBC/NoPadding") //exactly what we did in on create

        //cbc is  16 length  byte array so we have to make sure we have 16  items
        var tmp =data
        while(tmp.toByteArray().size%16 != 0){
            tmp+="\u0020"
        }
        cipher.init(Cipher.ENCRYPT_MODE, getKey())
        val  ivBytes = cipher.iv //init vector

        val encyptedBytes = cipher.doFinal(tmp.toByteArray(Charsets.UTF_8))

        return Pair(ivBytes, encyptedBytes)
    }

    private fun  decrypt(ivBytes: ByteArray, encryptedBytes: ByteArray): String{

            val cipher = Cipher.getInstance("AES/CBC/NoPadding")
            val specs = IvParameterSpec(ivBytes)
            cipher.init(Cipher.DECRYPT_MODE, getKey(), specs)

            return cipher.doFinal(encryptedBytes).toString(Charsets.UTF_8).trim()


    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun createDialog(){
        val view = View.inflate(this,R.layout.dialog_enter_value,null)
        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .setPositiveButton("Confirm"){_,_ ->
                //TODO Encrypt data form ET
                setUpKeyGenerator()
                val str = view.findViewById<EditText>(R.id.encrypt_value_edit_text).text.toString()
                pair = encrypt(str)

                println(pair.second.toString())
            }
            .create()
            .show()
    }
}