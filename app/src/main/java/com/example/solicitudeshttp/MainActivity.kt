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
import com.google.gson.Gson
import com.google.gson.JsonParser
import okhttp3.Call
import okhttp3.OkHttpClient
import org.json.JSONObject
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
                solicitudHttpVolley("https://swapi.dev/api/starships/")
            }
            else{
                Toast.makeText(this,"No hay una conexión de red!", Toast.LENGTH_LONG).show()
            }
        }

        bOkHttp.setOnClickListener {
            if (Network.hayRed(this)){
                solicitudHttpOk("https://swapi.dev/api/starships/")
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
                val json = JSONObject(response)
                val results = json.getJSONArray("results")
                for (i in 0..results.length()-1){
                    val name = results.getJSONObject(i).getString("name")
                    val model = results.getJSONObject(i).getString("model")
                    val manufacturer = results.getJSONObject(i).getString("manufacturer")
                    val starship_class = results.getJSONObject(i).getString("starship_class")
                    val startship = Startship(name,model,manufacturer,starship_class)
                    Log.d("Nave Numero", i.toString())
                    Log.d("Nombre", startship.name)
                    Log.d("Modelo", startship.model)
                    Log.d("Creado", startship.manufacturer)
                    Log.d("Clase", startship.starship_class)
                }

            }catch (e:Exception){
                Log.d("error",e.toString())
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
                Log.d("Error HttpOk",e.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val result = response.body?.string()
                this@MainActivity.runOnUiThread{
                    try {
                        if (result != null) {
                            val json = JSONObject(result)
                            val results = json.get("results").toString()
                            val list = ArrayList<Startship>()
                            val listresult:Array<Startship> = Gson().fromJson(results,Array<Startship>::class.java)
                            list.addAll(listresult)
                            Log.d("solicitudHttpOk", list.count().toString())
                        }

                    }catch (e:Exception){
                        Log.d("Error response",e.toString())
                    }

                }
            }
        })
    }
}