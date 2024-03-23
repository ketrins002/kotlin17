package com.example.kotlin17

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.kotlin17.adapter.PostsAdapter
import com.example.kotlin17.databinding.ActivityMainBinding
import com.example.kotlin17.model.Post
import org.json.JSONArray
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var workManager: WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        workManager = WorkManager.getInstance(applicationContext)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.loadButton.setOnClickListener {
            loadData()
        }
    }

    private fun loadData() {
        val workRequest = OneTimeWorkRequestBuilder<FetchDataWorker>().build()
        workManager.enqueue(workRequest)
        workManager.getWorkInfoByIdLiveData(workRequest.id).observe(this) { workInfo ->
            if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                loadDataFromLocalFile()
            }
        }
    }

    private fun loadDataFromLocalFile() {
        val filename = "posts_data.json"
        try {
            val jsonString = applicationContext.openFileInput(filename).bufferedReader().useLines { lines ->
                lines.fold("") { a, b -> "$a$b" }
            }
            Log.d("MainActivity:loadDataFromLocalFile", "jsonString: $jsonString")
            val posts = parsePostsJson(jsonString)
            binding.recyclerView.adapter = PostsAdapter(posts)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun parsePostsJson(jsonString: String): List<Post> {
        val postsList = mutableListOf<Post>()
        val jsonArray = JSONArray(jsonString)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val post = Post(
                userId = jsonObject.getInt("userId"),
                id = jsonObject.getInt("id"),
                title = jsonObject.getString("title"),
                body = jsonObject.getString("body")
            )
            postsList.add(post)
        }
        return postsList
    }
}
