package com.example.solicitudeshttp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.os.StrictMode
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import okhttp3.Call
import okhttp3.OkHttpClient
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity(), CompletadoListener {
    override fun descargaCompleta(resultado: String) {
        Log.d("descargaCompleta",resultado)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bValidarRed = findViewById<Button>(R.id.bValidarRed)
        val bSolicitudHttp = findViewById<Button>(R.id.bSolicitudHTTP)
        val bVolley = findViewById<Button>(R.id.bVolley)
        val bOkHttp = findViewById<Button>(R.id.bOkHttp)

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
//                Log.d("bSolicitudOnClick",descargarDatos("https://www.google.com"))
                DescargaURL(this).execute("https://www.google.com")
            }
            else{
                Toast.makeText(this,"No hay una conexión de red!",Toast.LENGTH_LONG).show()
            }
        }

        bVolley.setOnClickListener {
            if(Network.hayRed(this)){
                solicitudHttpVolley("https://www.google.com")
            }
            else{
                Toast.makeText(this,"No hay una conexión de red!", Toast.LENGTH_LONG).show()
            }
        }

        bOkHttp.setOnClickListener {
            if (Network.hayRed(this)){
                solicitudHttpOk("https://www.google.com")
            }
            else{
                Toast.makeText(this,"No hay una conexion de red", Toast.LENGTH_SHORT).show()
            }
        }


    }
   /* @Throws(IOException::class)
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
    }*/

    /*Metodo para volley*/
    private  fun solicitudHttpVolley(url:String){
        val queue = Volley.newRequestQueue(this)
        val solicitud = StringRequest(Request.Method.GET, url, Response.Listener<String>{
            response ->
            try{
                Log.d("solicitudHttpVolley", response)
            }catch (e:Exception){

            }

        },Response.ErrorListener {  })
        queue.add(solicitud)
    }

    //    Metodo para OK HTTP
    private fun solicitudHttpOk(url:String){
        val cliente = OkHttpClient()
        val solicitud = okhttp3.Request.Builder().url(url).build()
        cliente.newCall(solicitud).enqueue(object : okhttp3.Callback{
            override fun onFailure(call: Call, e: IOException) {
                //implementar error
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val result = response.body?.string()
                this@MainActivity.runOnUiThread{
                    try {
                        if (result != null) {
                            Log.d("solicitudHttpOk",result)
                        }

                    }catch (e:Exception){
                    }

                }
            }
        })
    }
}