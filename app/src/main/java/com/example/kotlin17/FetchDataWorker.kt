package com.example.kotlin17

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.IOException

class FetchDataWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        try {
            Thread.sleep(3000)
        } catch (e: InterruptedException) {
            return Result.failure()
        }
        val jsonData = NetworkService.fetchPostsJson() ?: return Result.failure()
        try {
            val filename = "posts_data.json"
            applicationContext.openFileOutput(filename, Context.MODE_PRIVATE).use {
                it.write(jsonData.toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return Result.failure()
        }
        return Result.success()
    }
}
