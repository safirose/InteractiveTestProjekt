package com.example.login

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.DataInput
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    lateinit var brugernavnInput: EditText
    lateinit var passwordInput: EditText
    lateinit var loginBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        brugernavnInput = findViewById(R.id.brugernavn_input)
        passwordInput = findViewById(R.id.kode_input)
        loginBtn = findViewById(R.id.login_btn)

        loginBtn.setOnClickListener{
            val brugernavn = brugernavnInput.text.toString()
            val kode = passwordInput.text.toString()
            Log.i("test","brugernavn:$brugernavn og kode:$kode")
        }
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets*/
        }
    }
