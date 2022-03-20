package com.example.lab4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.graphics.Bitmap
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONObject
import org.json.JSONException

class MainActivity : AppCompatActivity() {

    val imageButton: Button get() = findViewById(R.id.image_button)
    val myImageView: ImageView get() = findViewById(R.id.metArt)
    val descTextView: TextView get() = findViewById(R.id.desc_text)
    val urlView: TextView get() = findViewById(R.id.url_text)
    val url = "https://collectionapi.metmuseum.org/public/collection/v1/objects/"

    var rand = 0
    var primaryImage = ""
    fun getText() {
        val queue = Volley.newRequestQueue((this))
        rand = (400000..450000).random()
        urlView.text = url + "${rand}"

        val stringRequest = StringRequest(
            Request.Method.GET,
            urlView.text as String , { response ->
                descTextView.text = "Response From the MET: ${response.substring(0,200)}" }, {descTextView.text = "That didn't work!"})

        queue.add(stringRequest)

    }

    fun getTheImage(imageAddress: String) {
        val imageRequestQueue = Volley.newRequestQueue(this)
        val imageRequest = ImageRequest(imageAddress, { response ->
            myImageView.setImageBitmap(response) },
            500, 500, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888,
            {error -> Log.d("imageError", "Your request didn\'t go through")})


        imageRequestQueue.add(imageRequest)
    }

    fun getJSON(): String {
        var newurl = ""
        val queue = Volley.newRequestQueue((this))
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, urlView.text as String, null,
            Response.Listener { response -> Log.d("JSuccess","Response is %s".format(response.toString()))
                try {
                    val jsonObject = JSONObject(response.toString())
                    newurl = jsonObject.getString("primaryImage")
                    primaryImage = newurl

                    Log.d("JSuccess", "Object is in jsonObject, $newurl")
                    getTheImage(newurl)
                } catch (e: JSONException){
                    e.printStackTrace()
                }
                              }, Response.ErrorListener {error -> Log.d("jsonError","JSON request did not go through")})

        queue.add(jsonObjectRequest)
        return newurl
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageButton.setOnClickListener {
            getText()
            var primaryImage1 = getJSON()
            Log.d("Stringcheck", getJSON())
        }
    }

}