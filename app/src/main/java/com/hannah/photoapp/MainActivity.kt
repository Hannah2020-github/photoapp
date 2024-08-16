package com.hannah.photoapp

import android.os.Bundle
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

class MainActivity : AppCompatActivity() {
    private lateinit var searchText: EditText
    private lateinit var searchBtn: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerview: RecyclerView
    
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



    }
}
