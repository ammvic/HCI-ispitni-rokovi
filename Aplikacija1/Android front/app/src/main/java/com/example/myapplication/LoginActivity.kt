package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var fakultetApi: FakultetApi
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnGoToRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Retrofit instanca
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5134/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        fakultetApi = retrofit.create(FakultetApi::class.java)

        // Inicijalizacija input polja i dugmadi
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnGoToRegister = findViewById(R.id.btnGoToRegister)

        // Postavljanje click listener-a za dugme za login
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            val profesor = Profesor(0, username, password, "", "", "")
            login(profesor)
        }

        // Postavljanje click listener-a za dugme za registraciju
        btnGoToRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login(profesor: Profesor) {
        fakultetApi.login(profesor).enqueue(object : Callback<Profesor> {
            override fun onResponse(call: Call<Profesor>, response: Response<Profesor>) {
                if (response.isSuccessful) {
                    val intent = Intent(this@LoginActivity, PredmetiActivity::class.java)
                    intent.putExtra("profesorId", response.body()?.id)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Profesor>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
