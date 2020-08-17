package com.example.solicitudeshttp

import android.os.AsyncTask
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class DescargaURL(var completadoListener:CompletadoListener?):AsyncTask<String, Void, String>() {
    override fun doInBackground(vararg params: String): String {
        try {
            return descargarDatos(params[0])
        }catch (e:IOException){
            return ""
        }
    }

    override fun onPostExecute(result: String?) {
        try {
            if (result != null) {
                completadoListener?.descargaCompleta(result)
            }
        }catch (e:Exception){

        }
    }

    @Throws(IOException::class)
    private fun descargarDatos(liga:String):String{
        var inputStream : InputStream? = null
        try {
            val url = URL(liga)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"

            conn.connect()

            inputStream = conn.inputStream
            return inputStream.bufferedReader().use {
                it.readText()
            }

        } finally {
            if (inputStream != null){
                inputStream.close()
            }
        }
    }
}