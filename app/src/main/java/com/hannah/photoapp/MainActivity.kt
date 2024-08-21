package com.hannah.photoapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private val API_KEY = "TCWmoJU5NImbuUu28hDmP5tJJVodarAfh8HkNma9iSQJ4JOdXBF9JOE0"

    private lateinit var searchText: EditText
    private lateinit var searchBtn: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerview: RecyclerView
    private val handler = Handler(Looper.getMainLooper())
    private val picturesFromAPI: ArrayList<PictureData> = ArrayList()
    private val newIndices: ArrayList<Int> = ArrayList()
    private val cachedThreadPoolExecutor = Executors.newCachedThreadPool()

    private lateinit var layoutManager: LayoutManager
    private lateinit var adapter: Adapter
    // 需與 StaggeredGridLayoutManager 設定的 spanCount 數量一致(3)
    private val recyclerViewBottomImageContainer = intArrayOf(0, 0, 0)
    private var page = 1
    private val perPage = 15
    private var currentSearchingText = ""


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

        Thread {
            handler.post {
                searchBtn.isEnabled = false
                progressBar.visibility = View.VISIBLE
            }
            loadDataFromAPI("https://api.pexels.com/v1/curated?page=1&per_page=${perPage}")
            loadImageFromAPI()
//            for (i in 0 until picturesFromAPI.size) {
//                Log.d("AAA","{${picturesFromAPI[i].id}}, ${picturesFromAPI[i].medium}, ${picturesFromAPI[i].photographer}, ${picturesFromAPI[i].realImage}" )
//            }
            handler.post {
                layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
                adapter = Adapter(this, picturesFromAPI)
                recyclerview.layoutManager = layoutManager
                recyclerview.adapter = adapter

                progressBar.visibility = View.INVISIBLE
                searchBtn.isEnabled = true
            }
        }.start()

        recyclerview.addOnScrollListener(ScrollListener(this))

    }

    inner class ScrollListener(val context: Context): RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            // 類型轉換(as)成 StaggeredGridLayoutManager，才能 call findLastVisibleItemPositions()
            val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPositions(recyclerViewBottomImageContainer)
//                Log.d("AAA","${lastVisibleItemPosition[0]}")
//                Log.d("AAA","${lastVisibleItemPosition[1]}")
//                Log.d("AAA","${lastVisibleItemPosition[2]}")
            val itemCount = layoutManager.itemCount
            // 滑到圖片的最底端
            if (lastVisibleItemPosition[0] == itemCount - 1 || lastVisibleItemPosition[1] == itemCount - 1 || lastVisibleItemPosition[2] == itemCount - 1)
//                    Log.d("AAA","We are at the very bottom!")
                Thread {
                    handler.post {
                        searchBtn.isEnabled = false
                        progressBar.visibility = View.INVISIBLE
                        // applicationContext 是整個 App 的 Context，運用不當，較容易發生錯誤。
//                            Toast.makeText(applicationContext, resources.getString(R.string.new_image), Toast.LENGTH_SHORT).show()
                        Toast.makeText(context, resources.getString(R.string.new_image), Toast.LENGTH_SHORT).show()
                    }
                    page += 1
                    // 默認搜尋精選圖片
                    if (currentSearchingText == "") {
                        loadDataFromAPI("https://api.pexels.com/v1/curated?page=${page}&per_page=${perPage}")
                    }else {
                        loadDataFromAPI("https://api.pexels.com/v1/search?query=${currentSearchingText}&page=${page}&per_page=${perPage}")
                    }
                    loadImageFromAPI()
                    handler.post {
                        searchBtn.isEnabled = true
                        progressBar.visibility = View.VISIBLE
                        adapter.notifyDataSetChanged()
                    }
                }.start()
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
            val response = JSONObject(result)
            val photos = response.getJSONArray("photos")
            for (i in 0 until photos.length()) {
                val photo = photos.getJSONObject(i)
                picturesFromAPI.add(
                    PictureData(
                        photo.getString("id"),
                        photo.getString("photographer"),
                        photo.getJSONObject("src").getString("medium"),
                        null
                    )
                )
                newIndices.add(picturesFromAPI.size - 1)
            }
            inputStreamReader.close()
            bufferedReader.close()
            connection.disconnect()

        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadImageFromAPI() {
        val latch = CountDownLatch(newIndices.size)
        for (i in newIndices) {
            cachedThreadPoolExecutor.execute {
                try {
                    val inputStream: InputStream = URL(picturesFromAPI[i].medium).openStream()
                    picturesFromAPI[i].realImage = BitmapFactory.decodeStream(inputStream)
                    latch.countDown()
                }catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        try {
            latch.await()
        }catch (e: Exception) {
            e.printStackTrace()
        }
        newIndices.clear()
    }
}
