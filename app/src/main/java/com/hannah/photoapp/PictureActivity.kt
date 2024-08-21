package com.hannah.photoapp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hannah.photoapp.R

class PictureActivity : AppCompatActivity()  {
    private lateinit var imageView: ImageView
    private lateinit var idText: TextView
    private lateinit var authorText: TextView
    private lateinit var urlText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.picture_activity_layout)

        imageView = findViewById(R.id.image)
        idText = findViewById(R.id.id)
        authorText = findViewById(R.id.photographer)
        urlText = findViewById(R.id.url)

        idText.text = "Photo ID: ${intent.getStringExtra("id")}"
        authorText.text = "Author: ${intent.getStringExtra("author")}"
        urlText.text = "URL: ${intent.getStringExtra("url")}"

        // 獲取 intent 傳遞來的 byteArray 再轉換成 bitmap 顯示
        val byteArray = intent.getByteArrayExtra("bitmap")
        val image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
        imageView.setImageBitmap(image)
    }
}