package com.example.solicitudeshttp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bValidarRed = findViewById<Button>(R.id.bValidarRed)
        bValidarRed.setOnClickListener{
            //Código para validar red
            if(Network.hayRed(this)){
                Toast.makeText(this,"Si hay red!", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this,"NO hay una conexión de red!", Toast.LENGTH_LONG).show()

            }
        }
    }
}