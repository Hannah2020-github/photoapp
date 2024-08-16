package com.hannah.photoapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.RecyclerView
import com.hannah.photoapp.ui.theme.PhotoappTheme
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private val API_KEY = "TCWmoJU5NImbuUu28hDmP5tJJVodarAfh8HkNma9iSQJ4JOdXBF9JOE0"

    private lateinit var searchText: EditText
    private lateinit var searchBtn: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerview: RecyclerView
    private val singleThreadExecutor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        searchText = findViewById(R.id.search_text)
        searchBtn = findViewById(R.id.search_btn)
        progressBar = findViewById(R.id.progressBar)
        recyclerview = findViewById(R.id.recycler_view)
        progressBar.visibility = View.INVISIBLE

        searchBtn.setOnClickListener {
            Toast.makeText(this, "123~~", Toast.LENGTH_SHORT).show()
        }

        singleThreadExecutor.execute {
            handler.post {
                progressBar.visibility = View.VISIBLE
            }
            loadDataFromAPI("https://api.pexels.com/v1/curated?page=1&per_page=15")
            handler.post {
                progressBar.visibility = View.INVISIBLE
            }
        }

    }

    fun loadDataFromAPI(url: String) {
        val urlObject = URL(url)
        try {
            val connection: HttpURLConnection = urlObject.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Authorization", API_KEY)

            val inputStreamReader = InputStreamReader(connection.inputStream, "UTF-8")
            val bufferedReader = BufferedReader(inputStreamReader)
            val result = bufferedReader.readLine()
            Log.d("AAA", result)

        }catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
