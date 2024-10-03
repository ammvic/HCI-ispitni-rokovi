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
import android.util.Log


class RegisterActivity : AppCompatActivity() {

    private lateinit var fakultetApi: FakultetApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Retrofit instanca
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5134/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fakultetApi = retrofit.create(FakultetApi::class.java)

        val btnRegister = findViewById<Button>(R.id.btnRegister)
        btnRegister.setOnClickListener {
            val username = findViewById<EditText>(R.id.etUsername).text.toString()
            val password = findViewById<EditText>(R.id.etPassword).text.toString()
            val ime = findViewById<EditText>(R.id.etIme).text.toString()
            val prezime = findViewById<EditText>(R.id.etPrezime).text.toString()
            val oblast = findViewById<EditText>(R.id.etOblast).text.toString()

            val profesor = Profesor(0, username, password, ime, prezime, oblast)
            Log.d("RegisterActivity", "Registracija profesora: $profesor")
            register(profesor)
        }
    }

    private fun register(profesor: Profesor) {
        Log.d("RegisterActivity", "Slanje registracije za: ${profesor.username}")
        fakultetApi.register(profesor).enqueue(object : Callback<Profesor> {
            override fun onResponse(call: Call<Profesor>, response: Response<Profesor>) {
                if (response.isSuccessful) {
                    Log.d("RegisterActivity", "Registracija uspešna: ${response.body()}")
                    Toast.makeText(this@RegisterActivity, "Registracija uspešna!", Toast.LENGTH_SHORT).show()
                    // Nakon registracije preusmeri na LoginActivity
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("RegisterActivity", "Greška prilikom registracije: ${response.errorBody()?.string()}")
                    Toast.makeText(this@RegisterActivity, "Greška prilikom registracije", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Profesor>, t: Throwable) {
                Log.e("RegisterActivity", "Greška: ${t.message}", t)
                Toast.makeText(this@RegisterActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

