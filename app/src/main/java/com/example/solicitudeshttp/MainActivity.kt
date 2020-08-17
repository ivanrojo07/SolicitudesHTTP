package com.example.solicitudeshttp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Button
import android.widget.Toast
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bValidarRed = findViewById<Button>(R.id.bValidarRed)
        val bSolicitudHttp = findViewById<Button>(R.id.bSolicitudHTTP)

        bValidarRed.setOnClickListener{
            //Código para validar red
            if(Network.hayRed(this)){
                Toast.makeText(this,"Si hay red!", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this,"NO hay una conexión de red!", Toast.LENGTH_LONG).show()

            }
        }

        bSolicitudHttp.setOnClickListener{
            if (Network.hayRed(this)){
                Log.d("bSolicitudOnClick",descargarDatos("https://www.google.com"))
            }
            else{
                Toast.makeText(this,"No hay una conexión de red!",Toast.LENGTH_LONG).show()
            }
        }


    }
    @Throws(IOException::class)
    private fun descargarDatos(liga:String):String{

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        var inputStream:InputStream? = null
        try {
            val url = URL(liga)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.connect()
            inputStream = conn.inputStream
            return inputStream.bufferedReader().use {
                it.readText()
            }
        }finally {
            if (inputStream != null){
                inputStream.close()
            }
        }
    }
}